package com.juxinli;

import com.juxinli.common.JsonObjectMapper;
import com.juxinli.common.JsonResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * facebook and twitter 授权接口
 * Created by maybo on 17/7/17.
 */
@RestController
@RequestMapping("/oauth")
public class OauthController {

    @Autowired
    TwitterOauthServiceImpl twitterOauthService;


    @Autowired
    ValueOperations<String, Object> valueOperations;

    @Autowired
    TwitterOAuthSignature twitterOAuthSignature;

    @Autowired
    FaceBookOauthService faceBookOauthService;

    @Autowired
    RedisTemplate redisTemplate;

    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * twitter 授权通过参数用户的uid
     * @param uid
     * @return
     */

    @RequestMapping(value = "/twitter/authorize",name = "twitter重定向授权",method = RequestMethod.GET)
    public ModelAndView twitterAuthorize(String uid) {
        Map<String, String> dataMap = twitterOauthService.requestToken();//获取未授权的token
        if (dataMap != null) {
            String oauthToken = dataMap.get("oauth_token");
            String oauthSecret = dataMap.get("oauth_secret");
            dataMap.put("uid", uid);
            valueOperations.set(oauthToken, dataMap);
            ModelAndView mv = new ModelAndView("redirect:https://api.twitter.com/oauth/authorize?oauth_token=" + oauthToken);//redirect模式
            return mv;
        } else {
            ModelAndView mv = new ModelAndView();
            JsonResponse jsonResponse=JsonResponse.createError("获取未认证的授权码失败",null,401);
            mv.addObject(jsonResponse);
            return mv;
        }
    }

    /**
     * 获取用户授权回调的临时verifier
     * @param oauth_token
     * @param oauth_verifier
     * @return
     */
    @RequestMapping(value = "/twitter/verifier",method = RequestMethod.GET)
    public JsonResponse queryString(String oauth_token, String oauth_verifier) {
        Map<String, String> dataMap = (Map) valueOperations.get(oauth_token);
        if (dataMap != null) {
            String oauthToken = dataMap.get("oauth_token");
            String oauthSecret = dataMap.get("oauth_secret");
            String uid = dataMap.get("uid");
            String result = twitterOauthService.accessToken(oauthToken, oauthSecret, oauth_verifier);
            if (null != result) {
                String[] resultArray = result.split("&");
                oauthToken = resultArray[0].split("=")[1];
                oauthSecret = resultArray[1].split("=")[1];
                dataMap.put("oauth_token", oauthToken);
                dataMap.put("oauth_secret", oauthSecret);
                valueOperations.set("twitter-" + uid, dataMap, 1, TimeUnit.DAYS);
                redisTemplate.delete(oauth_token);
                return JsonResponse.createData(dataMap);
            } else {
                return JsonResponse.createError("认证失败!", new Exception("用户认证失败!"),401);
            }
        }
        return JsonResponse.createError("获取临时token_secert信息失败!", new Exception("获取临时token_secert信息失败!"));

    }

    /**
     * 获取twitter的authorization
     * @param uid
     * @param url
     * @param httpMethod
     * @param params
     * @return
     */
    @RequestMapping("/twitter/apiToken")
    public JsonResponse apiTwitterToken(String uid, String url, String httpMethod, String params) {
        Map<String, String> dataMap = (Map) valueOperations.get("twitter-" + uid);
        String oauthToken = dataMap.get("oauth_token");
        String oauthSecret = dataMap.get("oauth_secret");
        if (params != null) {
            Map<String, String> paramMap = (Map<String, String>) JsonObjectMapper.readerValueAsObject(params, Map.class);
            paramMap.put("oauth_token", oauthToken);
            return JsonResponse.createData(twitterOAuthSignature.apiToken(paramMap, oauthSecret, url, httpMethod));
        } else {
            try {
                return JsonResponse.createData(twitterOAuthSignature.apiToken(oauthToken, oauthSecret, url, httpMethod));
            } catch (UnsupportedEncodingException e) {
              logger.error("编码异常",e);
                return JsonResponse.createError("编码异常", e);
            }
        }
    }

    /**
     * 获取facebook重定向的code
     * @param code
     * @param state
     * @return
     */
    @RequestMapping(value = "/facebook/code",method = RequestMethod.GET)
    public JsonResponse queryCode(String code, String state) {

        JsonResponse jsonResponse = faceBookOauthService.getAccessToken(code);

        if (null != jsonResponse) {
            if (jsonResponse.getCode() == 200) {
                valueOperations.set("facebook-" + state, (Map) jsonResponse.getData(),1,TimeUnit.DAYS);
            } else {
                return jsonResponse;
            }
        }
        return JsonResponse.createError("获取access_token为空!", new NullPointerException());

    }

    /**
     * 获取facebook的access_token
     * @param uid
     * @return
     */
    @RequestMapping(value = "/facebook/apiToken",method = RequestMethod.GET)
    public JsonResponse apiFaceBookToken(String uid) {
        try {
            Map<String, String> dataMap = (Map) valueOperations.get("facebook-" + uid);
            if (null != dataMap) {
                String access_token = dataMap.get("access_token");
                return JsonResponse.createData(access_token);
            } else {
                return JsonResponse.createError("未授权或授权码已经过期", null, 401);
            }
        } catch (Exception e) {
            logger.error(e);
            return JsonResponse.createError("数据获取异常 ！", e);
        }

    }

    /**
     * facebook授权接口
     * @param uid
     * @return
     */
    @RequestMapping(value = "/facebook/authorize",method = RequestMethod.GET)
    public ModelAndView facebookAuthorize(String uid) {
        return faceBookOauthService.facebookAuthorize(uid);
    }

}
