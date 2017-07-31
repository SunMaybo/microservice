package com.juxinli;

import com.juxinli.spinner.CollectTemplate;
import com.juxinli.spinner.HttpClientUtils;
import com.juxinli.spinner.SpinnerResponse;
import com.juxinli.template.JRestTemplate;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maybo on 17/7/17.
 */
@Service
public class TwitterOauthServiceImpl {

    private String requestTokenUrl = "https://api.twitter.com/oauth/request_token";

    private String authorize = "https://api.twitter.com/oauth/authorize";

    private String accessTokenUrl = "https://api.twitter.com/oauth/access_token";

    @Value("${twitter.oauth1.0a.callback}")
    private String callBack;

    @Autowired
    private TwitterOAuthSignature twitterOAuthSignature;

    @Autowired
    private CollectTemplate collectTemplate;

    public Map requestToken() {

        try {
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("oauth_callback", callBack);
            Headers.Builder headBuilder = new Headers.Builder();
            headBuilder.add("Content-Type", "application/x-www-form-urlencoded");
            String authorization = twitterOAuthSignature.token();
            headBuilder.add("Authorization", authorization);
            SpinnerResponse response = collectTemplate.post(requestTokenUrl, builder.build(), headBuilder.build());
            if (response != null) {
                if (response.getCode() == 200) {

                    String result = response.getData();
                    if (result != null) {
                        String[] resultArray = result.split("&");
                        String oauth_token = resultArray[0].split("=")[1];
                        String oauth_secret = resultArray[1].split("=")[1];
                        String oauth_callback_confirmed = resultArray[2].split("=")[1];
                        if ("true".equals(oauth_callback_confirmed)) {
                            Map<String, String> dataMap = new HashMap<>();
                            dataMap.put("oauth_token", oauth_token);
                            dataMap.put("oauth_secret", oauth_secret);
                            return dataMap;
                        }
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String accessToken(String oauthToken, String oauthSecret, String oauthVerifier) {

        try {
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("oauth_verifier", oauthVerifier);
            Headers.Builder headBuilder = new Headers.Builder();
            headBuilder.add("Content-Type", "application/x-www-form-urlencoded");
            String authorization = twitterOAuthSignature.accessToken(oauthToken, oauthSecret);
            headBuilder.add("Authorization", authorization);
            SpinnerResponse response = collectTemplate.post(accessTokenUrl, builder.build(), headBuilder.build());
            if (response != null) {
                if (response.getCode() == 200) {
                    String result = response.getData();
                    if (result != null) {
                       return result;
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
