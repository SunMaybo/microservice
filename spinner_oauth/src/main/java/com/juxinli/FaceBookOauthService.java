package com.juxinli;

import com.juxinli.common.JsonObjectMapper;
import com.juxinli.common.JsonResponse;
import com.juxinli.spinner.CollectTemplate;
import com.juxinli.spinner.SpinnerResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * facebook授权服务
 * Created by maybo on 17/7/20.
 */
@Service
public class FaceBookOauthService {

    @Value("${facebook.client_id}")
    private String clientId;
    @Value("${facebook.client_secret}")
    private String clientSecret;
    @Value("${facebook.callback}")
    private String callback;

    private String accessTokenUrl = "https://graph.facebook.com/oauth/access_token";

    private String authorizeUrl = "https://graph.facebook.com/oauth/authorize";


    @Autowired
    private CollectTemplate collectTemplate;

    public ModelAndView facebookAuthorize(String uid) {

        authorizeUrl = authorizeUrl + "?client_id=" + clientId + "&redirect_uri=" + callback + "&state=" + uid + "&scope=email public_profile user_friends";
        ModelAndView mv = new ModelAndView("redirect:" + authorizeUrl);//redirect模式
        return mv;
    }

    public JsonResponse getAccessToken(String code) {
        SpinnerResponse response = collectTemplate.get(accessTokenUrl + "?client_id=" + clientId + "&client_secret=" + clientSecret + "&code=" + code + "&redirect_uri=" + callback);

        if (response.getCode() == 200) {
            return JsonResponse.createData((Map) JsonObjectMapper.readerValueAsObject(response.getData(), Map.class));
        } else {

            return JsonResponse.createError(response.getDescription(),response.getException(),response.getCode());

        }
    }
}
