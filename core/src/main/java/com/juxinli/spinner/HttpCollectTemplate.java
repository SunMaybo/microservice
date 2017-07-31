package com.juxinli.spinner;

import okhttp3.OkHttpClient;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Created by maybo on 17/6/28.
 */
public class HttpCollectTemplate extends CollectTemplate {

    private static final HttpCollectTemplate HTTP_COLLECT_TEMPLATE=new HttpCollectTemplate();
    @Override
    public CollectTemplate create(long readTimeOut, long connectTimeOut, long writeTimeOut, String cookieCode, long cookieTimeOut, RedisTemplate<String, Object> redisTemplate) {
        return null;
    }

    @Override
    public CollectTemplate create(long readTimeOut, long connectTimeOut, long writeTimeOut) {
        synchronized (HTTP_COLLECT_TEMPLATE) {
            if (HTTP_COLLECT_TEMPLATE.getOkHttpClient() == null) {
                HTTP_COLLECT_TEMPLATE.setOkHttpClient(new OkHttpClient
                        .Builder()
                                .connectTimeout(readTimeOut, TimeUnit.SECONDS)
                                .writeTimeout(connectTimeOut, TimeUnit.SECONDS)
                                        //.retryOnConnectionFailure(false)
                                .readTimeout(writeTimeOut, TimeUnit.SECONDS)
                        .build()
               );
            } else {
                return HTTP_COLLECT_TEMPLATE;
            }
        }
        return HTTP_COLLECT_TEMPLATE;
    }
}
