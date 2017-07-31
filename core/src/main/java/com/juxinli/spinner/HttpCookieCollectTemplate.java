package com.juxinli.spinner;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by maybo on 17/6/28.
 */
public class HttpCookieCollectTemplate extends CollectTemplate {


    public CollectTemplate create(long readTimeOut, long connectTimeOut, long writeTimeOut, String cookieCode, long cookieTimeOut, RedisTemplate<String, Object> redisTemplate) {
        CollectTemplate collectTemplate = new HttpCookieCollectTemplate();
        collectTemplate.setCookieCode(cookieCode);
        collectTemplate.setTimeout(cookieTimeOut);
        collectTemplate.setOkHttpClient(new OkHttpClient
                .Builder()
                .cookieJar(new CookieJar() {

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

                        Map<String, CookieBO> cookieMap = (Map) redisTemplate.opsForValue().get(collectTemplate.getCookieCode());
                        if (null == cookieMap) {
                            cookieMap = new HashMap<String, CookieBO>();

                        }
                        for (Cookie cookie : cookies) {
                                 CookieBO cookieBO =new CookieBO(cookie.name(),cookie.value(),cookie.expiresAt(),cookie.domain(),cookie.path(),cookie.secure(),cookie.httpOnly(),cookie.persistent(),cookie.hostOnly());
                            cookieMap.put(cookie.name(),cookieBO );

                        }

                        redisTemplate.opsForValue().set(collectTemplate.getCookieCode(), cookieMap, collectTemplate.getTimeout());
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookieList = new ArrayList<Cookie>();
                        Map<String, Map> cookieMap = (Map) redisTemplate.opsForValue().get(collectTemplate.getCookieCode());
                        if (null != cookieMap) {
                            Iterator iterator = cookieMap.keySet().iterator();
                            while (iterator.hasNext()) {
                                String name = (String) iterator.next();
                                Map dataMap=cookieMap.get(name);
                                Cookie.Builder builder = new Cookie.
                                        Builder().name(name).value((String)dataMap.get("value"))
                                        .expiresAt((Long) dataMap.get("expiresAt"))
                                        .domain((String)dataMap.get("domain"))
                                        .path((String)dataMap.get("path"));
                                if ((boolean)dataMap.get("hostOnly")){
                                    builder.hostOnlyDomain((String)dataMap.get("domain"));
                                }
                                if ((boolean)dataMap.get("httpOnly")){
                                    builder.httpOnly();
                                }
                                if ((boolean)dataMap.get("secure")){
                                    builder.secure();
                                }
                                cookieList.add(builder.build());
                            }
                        }
                        return cookieList;
                    }
                })
                .connectTimeout(readTimeOut, TimeUnit.SECONDS)
                .writeTimeout(connectTimeOut, TimeUnit.SECONDS)
                        //.retryOnConnectionFailure(false)
                .readTimeout(writeTimeOut, TimeUnit.SECONDS)
                .build());

        return collectTemplate;

    }

    @Override
    public CollectTemplate create(long readTimeOut, long connectTimeOut, long writeTimeOut) {
        return null;
    }
}
