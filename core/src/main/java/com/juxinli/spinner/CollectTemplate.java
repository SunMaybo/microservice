package com.juxinli.spinner;

import okhttp3.*;
import org.apache.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;

public abstract class CollectTemplate implements Template {

    static Logger logger = Logger.getLogger(CollectTemplate.class);

    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient okHttpClient = null;

    private String cookieCode;

    private long timeout;

    public void setCookieCode(String cookieCode) {
        this.cookieCode = cookieCode;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getTimeout() {
        return timeout;
    }

    public String getCookieCode() {
        return cookieCode;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }


    protected CollectTemplate() {


    }


    public abstract CollectTemplate create(long readTimeOut, long connectTimeOut, long writeTimeOut, String cookieCode, long cookieTimeOut, RedisTemplate<String, Object> redisTemplate);

    public abstract CollectTemplate create(long readTimeOut, long connectTimeOut, long writeTimeOut);

    private SpinnerResponse response(Response response,String url) {
        SpinnerResponse spinnerResponse = new SpinnerResponse();

        try {
            spinnerResponse.setData(response.body().string());
            spinnerResponse.setDescription(response.message());
            spinnerResponse.setCode(response.code());
            if (response.code() != 200) {
                logger.warn("接口调用非正常----url---"+url+"----" + spinnerResponse.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            spinnerResponse.setDescription(response.message());
            spinnerResponse.setCode(response.code());
            spinnerResponse.setException(e);
            logger.error("IO异常----url----"+url+"----" + spinnerResponse.toString());

        }
        response.close();

        return spinnerResponse;
    }

    private SpinnerResponse response(Exception e, String message,String url) {
        SpinnerResponse spinnerResponse = new SpinnerResponse();


        spinnerResponse.setDescription(message);

        spinnerResponse.setException(e);

        spinnerResponse.setCode(-404);


        logger.error("IO异常----url---"+url+"----" + spinnerResponse.toString());

        return spinnerResponse;
    }

    public SpinnerResponse get(String url) {

        Request request = new Request
                .Builder()
                .url(url)
                .get()
                .build();
        Response response = null;

        try {
            response = okHttpClient
                    .newCall(request)
                    .execute();
            return response(response,url);
        } catch (IOException e) {
            e.printStackTrace();
            return response(e, e.getMessage(),url);
        }

    }

    public SpinnerResponse get(String url, Headers headers) {

        Request request = new Request
                .Builder()
                .headers(headers)
                .url(url)
                .get()
                .build();
        Response response = null;
        try {
            response = okHttpClient
                    .newCall(request)
                    .execute();
            return response(response,url);
        } catch (IOException e) {
            e.printStackTrace();
            return response(e, e.getMessage(),url);
        }
    }

    public SpinnerResponse post(String url, String jsonStrData, Headers headers) {


        RequestBody requestBody = RequestBody.create(JSON_TYPE, jsonStrData);

        try {
            Request request = new Request
                    .Builder()
                    .headers(headers)
                    .addHeader("Connection", "false")
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response = null;
            response = okHttpClient
                    .newCall(request)
                    .execute();


            return response(response,url);
        } catch (IOException e) {
            e.printStackTrace();
            return response(e, e.getMessage(),url);
        }


    }


    public SpinnerResponse post(String url, FormBody param, Headers headers) {


        try {
            Request request = new Request
                    .Builder()
                    .headers(headers)
                    .addHeader("Connection", "false")
                    .url(url)
                    .post(param)
                    .build();
            Response response = null;
            response = okHttpClient
                    .newCall(request)
                    .execute();


            return response(response,url);
        } catch (IOException e) {
            e.printStackTrace();
            return response(e, e.getMessage(),url);
        }

    }

    public SpinnerResponse post(String url, Headers headers) {


        try {
            Request request = new Request
                    .Builder()
                    .headers(headers)
                    .addHeader("Connection", "false")
                    .url(url)
                    .build();
            Response response = null;
            response = okHttpClient
                    .newCall(request)
                    .execute();


            return response(response,url);
        } catch (IOException e) {
            e.printStackTrace();
            return response(e, e.getMessage(),url);
        }

    }

    public SpinnerResponse post(String url) {


        try {
            Request request = new Request
                    .Builder()
                    .addHeader("Connection", "false")
                    .url(url)
                    .build();
            Response response = null;
            response = okHttpClient
                    .newCall(request)
                    .execute();


            return response(response,url);
        } catch (IOException e) {
            e.printStackTrace();
            return response(e, e.getMessage(),url);
        }
    }

    public SpinnerResponse post(String url, FormBody param) {


        try {
            Request request = new Request
                    .Builder()
                    .addHeader("Connection", "false")
                    .url(url)
                    .post(param)
                    .build();
            Response response = null;
            response = okHttpClient
                    .newCall(request)
                    .execute();


            return response(response,url);
        } catch (IOException e) {
            e.printStackTrace();
            return response(e, e.getMessage(),url);
        }
    }

    public static void main(String[] args) {

    }

    public SpinnerResponse post(String url, String jsonStrData) {

        RequestBody requestBody = RequestBody.create(JSON_TYPE, jsonStrData);

        try {
            Request request = new Request
                    .Builder()
                    .addHeader("Connection", "false")
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response = null;
            response = okHttpClient
                    .newCall(request)
                    .execute();


            return response(response,url);
        } catch (IOException e) {
            e.printStackTrace();
            return response(e, e.getMessage(),url);
        }
    }
}
