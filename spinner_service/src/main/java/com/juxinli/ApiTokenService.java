package com.juxinli;

import com.juxinli.common.JsonObjectMapper;
import com.juxinli.common.JsonResponse;
import com.juxinli.spinner.CollectTemplate;
import com.juxinli.spinner.SpinnerResponse;
import okhttp3.FormBody;
import okhttp3.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * 获取授权信息来自spinner_oauth服务模块
 * Created by maybo on 17/7/18.
 */
@Service
public class ApiTokenService {

    @Autowired
    private CollectTemplate collectTemplate;

    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * 获取twitter授权信息
     * @param uid
     * @param url
     * @param httpMethod
     * @return
     */
    public String apiTwitterToken(String uid, String url, String httpMethod) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("uid", uid);
        builder.add("url", url);
        builder.add("httpMethod", httpMethod);
        SpinnerResponse response = collectTemplate.post("http://127.0.0.1:8956/jxl_oauth/oauth/twitter/apiToken", builder.build());
        if (response.getCode() == 200) {

            return response.getData();

        }
        logger.warn(response.toString());
        return null;
    }

    /**
     * 获取twitter授权信息
     * @param uid
     * @param url
     * @param httpMethod
     * @param params
     * @return
     */
    public String apiTwitterToken(String uid, String url, String httpMethod, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("uid", uid);
        builder.add("url", url);
        builder.add("httpMethod", httpMethod);
        if (params != null) {
            builder.add("params", JsonObjectMapper.writeValueAsString(params));
        }
        SpinnerResponse response = collectTemplate.post("http://127.0.0.1:8956/jxl_oauth/oauth/twitter/apiToken", builder.build());
        if (response.getCode() == 200) {

            return response.getData();

        }
        logger.warn(response.toString());
        return null;
    }

    /**
     * 获取Facebook 授权信息
     * @param uid
     * @return
     */
    public String apiFaceBookToken(String uid) {

        SpinnerResponse response = collectTemplate.get("http://127.0.0.1:8956/jxl_oauth/oauth/facebook/apiToken?uid=" + uid);
        if (response.getCode() == 200) {

            JsonResponse jsonResponse = (JsonResponse) JsonObjectMapper.readerValueAsObject(response.getData(), JsonResponse.class);
            return jsonResponse.getData().toString();

        }
        logger.warn(response.toString());
        return null;
    }
}
