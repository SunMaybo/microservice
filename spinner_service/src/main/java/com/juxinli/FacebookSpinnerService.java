package com.juxinli;

import com.juxinli.common.JsonObjectMapper;
import com.juxinli.common.JsonResponse;
import com.juxinli.spinner.CollectTemplate;
import com.juxinli.spinner.SpinnerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * facebook数据抓取服务
 * Created by maybo on 17/7/20.
 */
@Service
public class FacebookSpinnerService {

    private String url = "https://graph.facebook.com/me";

    @Autowired
    private CollectTemplate collectTemplate;

    @Autowired
    private ApiTokenService apiTokenService;

    public JsonResponse getInfo(String uid) {
        String accessToken = apiTokenService.apiFaceBookToken(uid);

        if (null==accessToken){
            return JsonResponse.createError("获取授权信息失败",null,401);
        }

        url=url+"?access_token="+accessToken+"&fields="+
                "id,name,birthday,education,currency,email,favorite_athletes,favorite_teams,first_name,hometown,gender,inspirational_people,languages,last_name,link,locale,location,middle_name,relationship_status,third_party_id,name_format,quotes,sports,religion,updated_time,timezone,website,work,family,groups,friends{email,id,name},friendlists{name,id},likes,books,movies,music,games";

        SpinnerResponse spinnerResponse= collectTemplate.get(url);
        if (spinnerResponse.getCode()==200){
            return JsonResponse.createData((Map)JsonObjectMapper.readerValueAsObject(spinnerResponse.getData(),Map.class));
        }
        return JsonResponse.createError(spinnerResponse.getDescription(),spinnerResponse.getException(),spinnerResponse.getCode());
    }
}
