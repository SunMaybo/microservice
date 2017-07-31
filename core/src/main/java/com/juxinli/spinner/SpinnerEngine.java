package com.juxinli.spinner;

import okhttp3.FormBody;
import okhttp3.Headers;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by maybo on 17/6/28.
 */
public class SpinnerEngine implements SpinnerTemplate {

    private CollectTemplate collectTemplate;

    private RedisTemplate<String,Object>redisTemplate;

    private long readTimeOut=6;
    private long writeTimeOut=6;
    private long connectTimeOut=120;

    public SpinnerEngine(CollectTemplate collectTemplate,long readTimeOut,long writeTimeOut,long connectTimeOut){
        this.collectTemplate=collectTemplate;
        this.readTimeOut=readTimeOut;
        this.connectTimeOut=connectTimeOut;
        this.writeTimeOut=writeTimeOut;
    }

    public SpinnerEngine(CollectTemplate collectTemplate){
        this.collectTemplate=collectTemplate;
    }

    public SpinnerEngine(CollectTemplate collectTemplate,long readTimeOut,long writeTimeOut,long connectTimeOut,RedisTemplate<String,Object> redisTemplate){
        this.collectTemplate=collectTemplate;
        this.readTimeOut=readTimeOut;
        this.connectTimeOut=connectTimeOut;
        this.writeTimeOut=writeTimeOut;
        this.redisTemplate=redisTemplate;
    }

    public SpinnerEngine(CollectTemplate collectTemplate,RedisTemplate<String,Object> redisTemplate){
        this.collectTemplate=collectTemplate;
        this.redisTemplate=redisTemplate;

    }

    @Override
    public SpinnerResponse get(String url, ResponseProcessor responseProcessor) {
        SpinnerResponse spinnerResponse =collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut).get(url);
        ResponseProcessorFactory.execute(spinnerResponse,responseProcessor );
        return spinnerResponse;
    }

    @Override
    public SpinnerResponse get(String url, Headers headers, ResponseProcessor responseProcessor) {
        SpinnerResponse spinnerResponse =collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut).get(url, headers);
        ResponseProcessorFactory.execute(spinnerResponse,responseProcessor );
        return spinnerResponse;
    }

    @Override
    public SpinnerResponse post(String url, String jsonStrData, Headers headers, ResponseProcessor responseProcessor) {
        SpinnerResponse spinnerResponse =collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut).post(url, jsonStrData, headers);
        ResponseProcessorFactory.execute(spinnerResponse,responseProcessor );
        return spinnerResponse;
    }

    @Override
    public SpinnerResponse post(String url, FormBody param, Headers headers, ResponseProcessor responseProcessor) {
        SpinnerResponse spinnerResponse =collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut).post(url, param, headers);
        ResponseProcessorFactory.execute(spinnerResponse,responseProcessor );
        return spinnerResponse;
    }

    @Override
    public SpinnerResponse post(String url, Headers headers, ResponseProcessor responseProcessor) {
        SpinnerResponse spinnerResponse =collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut).post(url, headers);
        ResponseProcessorFactory.execute(spinnerResponse,responseProcessor );
        return spinnerResponse;
    }

    @Override
    public SpinnerResponse post(String url, ResponseProcessor responseProcessor) {
        SpinnerResponse spinnerResponse =collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut).post(url);
        ResponseProcessorFactory.execute(spinnerResponse,responseProcessor );
        return spinnerResponse;
    }

    @Override
    public SpinnerResponse post(String url, FormBody param, ResponseProcessor responseProcessor) {
        SpinnerResponse spinnerResponse =collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut).post(url, param);
        ResponseProcessorFactory.execute(spinnerResponse,responseProcessor );
        return spinnerResponse;
    }

    @Override
    public SpinnerResponse post(String url, String jsonStrData, ResponseProcessor responseProcessor) {
        SpinnerResponse spinnerResponse =collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut).post(url, jsonStrData);
        ResponseProcessorFactory.execute(spinnerResponse,responseProcessor );
        return spinnerResponse;
    }

    @Override
    public SpinnerResponse get(String url, long cookieTimeOut, String cookieCode, ResponseProcessor responseProcessor) {
        if (cookieCode==null||cookieTimeOut<=0|| redisTemplate==null){
            throw new NullPointerException("关于cookie持久化配置不可以为空!");
        }
        SpinnerResponse spinnerResponse =collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut,cookieCode,cookieTimeOut,redisTemplate).get(url);
        ResponseProcessorFactory.execute(spinnerResponse,responseProcessor );
        return spinnerResponse;
    }

    @Override
    public SpinnerResponse get(String url, Headers headers, long cookieTimeOut, String cookieCode, ResponseProcessor responseProcessor) {
        if (cookieCode==null||cookieTimeOut<=0|| redisTemplate==null){
            throw new NullPointerException("关于cookie持久化配置不可以为空!");
        }
        SpinnerResponse spinnerResponse =collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut,cookieCode,cookieTimeOut,redisTemplate).get(url,headers);
        ResponseProcessorFactory.execute(spinnerResponse,responseProcessor );
        return spinnerResponse;
    }

    @Override
    public SpinnerResponse post(String url, String jsonStrData, Headers headers, long cookieTimeOut, String cookieCode, ResponseProcessor responseProcessor) {
        if (cookieCode==null||cookieTimeOut<=0|| redisTemplate==null){
            throw new NullPointerException("关于cookie持久化配置不可以为空!");
        }
        SpinnerResponse spinnerResponse =collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut,cookieCode,cookieTimeOut,redisTemplate).post(url, jsonStrData, headers);
        ResponseProcessorFactory.execute(spinnerResponse,responseProcessor );
        return spinnerResponse;
    }

    @Override
    public SpinnerResponse post(String url, FormBody param, Headers headers, long cookieTimeOut, String cookieCode, ResponseProcessor responseProcessor) {
        if (cookieCode==null||cookieTimeOut<=0|| redisTemplate==null){
            throw new NullPointerException("关于cookie持久化配置不可以为空!");
        }
        SpinnerResponse spinnerResponse =collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut,cookieCode,cookieTimeOut,redisTemplate).post(url,param,headers);
        ResponseProcessorFactory.execute(spinnerResponse,responseProcessor );
        return spinnerResponse;
    }

    @Override
    public SpinnerResponse post(String url, Headers headers, long cookieTimeOut, String cookieCode, ResponseProcessor responseProcessor) {
        if (cookieCode==null||cookieTimeOut<=0|| redisTemplate==null){
            throw new NullPointerException("关于cookie持久化配置不可以为空!");
        }
        SpinnerResponse spinnerResponse =collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut,cookieCode,cookieTimeOut,redisTemplate).post(url,headers);
        ResponseProcessorFactory.execute(spinnerResponse,responseProcessor );
        return spinnerResponse;
    }

    @Override
    public SpinnerResponse post(String url, long cookieTimeOut, String cookieCode, ResponseProcessor responseProcessor) {
        if (cookieCode==null||cookieTimeOut<=0|| redisTemplate==null){
            throw new NullPointerException("关于cookie持久化配置不可以为空!");
        }
        SpinnerResponse spinnerResponse =collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut,cookieCode,cookieTimeOut,redisTemplate).post(url);
        ResponseProcessorFactory.execute(spinnerResponse,responseProcessor );
        return spinnerResponse;
    }

    @Override
    public SpinnerResponse post(String url, FormBody param, long cookieTimeOut, String cookieCode, ResponseProcessor responseProcessor) {
        if (cookieCode==null||cookieTimeOut<=0|| redisTemplate==null){
            throw new NullPointerException("关于cookie持久化配置不可以为空!");
        }

        SpinnerResponse spinnerResponse =collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut,cookieCode,cookieTimeOut,redisTemplate).post(url,param);
        ResponseProcessorFactory.execute(spinnerResponse,responseProcessor );
        return spinnerResponse;
    }

    @Override
    public SpinnerResponse post(String url, String jsonStrData, long cookieTimeOut, String cookieCode, ResponseProcessor responseProcessor) {
        if (cookieCode==null||cookieTimeOut<=0|| redisTemplate==null){
            throw new NullPointerException("关于cookie持久化配置不可以为空!");
        }
        SpinnerResponse spinnerResponse =collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut,cookieCode,cookieTimeOut,redisTemplate).post(url,jsonStrData);
        ResponseProcessorFactory.execute(spinnerResponse,responseProcessor );
        return spinnerResponse;
    }

    @Override
    public SpinnerResponse get(String url, long cookieTimeOut, String cookieCode) {
        if (cookieCode==null||cookieTimeOut<=0|| redisTemplate==null){
            throw new NullPointerException("关于cookie持久化配置不可以为空!");
        }
        return collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut,cookieCode,cookieTimeOut,redisTemplate).get(url);
    }

    @Override
    public SpinnerResponse get(String url, Headers headers, long cookieTimeOut, String cookieCode) {
        if (cookieCode==null||cookieTimeOut<=0|| redisTemplate==null){
            throw new NullPointerException("关于cookie持久化配置不可以为空!");
        }
        return collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut,cookieCode,cookieTimeOut,redisTemplate).get(url,headers);
    }

    @Override
    public SpinnerResponse post(String url, String jsonStrData, Headers headers, long cookieTimeOut, String cookieCode) {
        if (cookieCode==null||cookieTimeOut<=0|| redisTemplate==null){
            throw new NullPointerException("关于cookie持久化配置不可以为空!");
        }
        return collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut,cookieCode,cookieTimeOut,redisTemplate).post(url, jsonStrData, headers);
    }

    @Override
    public SpinnerResponse post(String url, FormBody param, Headers headers, long cookieTimeOut, String cookieCode) {
        if (cookieCode==null||cookieTimeOut<=0|| redisTemplate==null){
            throw new NullPointerException("关于cookie持久化配置不可以为空!");
        }
        return collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut,cookieCode,cookieTimeOut,redisTemplate).post(url,param,headers);
    }

    @Override
    public SpinnerResponse post(String url, Headers headers, long cookieTimeOut, String cookieCode) {
        if (cookieCode==null||cookieTimeOut<=0|| redisTemplate==null){
            throw new NullPointerException("关于cookie持久化配置不可以为空!");
        }
        return collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut,cookieCode,cookieTimeOut,redisTemplate).post(url,headers);
    }

    @Override
    public SpinnerResponse post(String url, long cookieTimeOut, String cookieCode) {
        if (cookieCode==null||cookieTimeOut<=0|| redisTemplate==null){
            throw new NullPointerException("关于cookie持久化配置不可以为空!");
        }
        return collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut,cookieCode,cookieTimeOut,redisTemplate).post(url);
    }

    @Override
    public SpinnerResponse post(String url, FormBody param, long cookieTimeOut, String cookieCode) {
        if (cookieCode==null||cookieTimeOut<=0|| redisTemplate==null){
            throw new NullPointerException("关于cookie持久化配置不可以为空!");
        }
        return collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut,cookieCode,cookieTimeOut,redisTemplate).post(url,param);
    }

    @Override
    public SpinnerResponse post(String url, String jsonStrData, long cookieTimeOut, String cookieCode) {
        if (cookieCode==null||cookieTimeOut<=0|| redisTemplate==null){
            throw new NullPointerException("关于cookie持久化配置不可以为空!");
        }
        return collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut,cookieCode,cookieTimeOut,redisTemplate).post(url,jsonStrData);
    }

    @Override
    public SpinnerResponse get(String url) {
        return collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut).get(url);
    }

    @Override
    public SpinnerResponse get(String url, Headers headers) {
        return collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut).get(url, headers);
    }

    @Override
    public SpinnerResponse post(String url, String jsonStrData, Headers headers) {
        return collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut).post(url, jsonStrData, headers);
    }

    @Override
    public SpinnerResponse post(String url, FormBody param, Headers headers) {
        return collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut).post(url, param, headers);
    }

    @Override
    public SpinnerResponse post(String url, Headers headers) {
        return collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut).post(url, headers);
    }

    @Override
    public SpinnerResponse post(String url) {
        return collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut).post(url);
    }

    @Override
    public SpinnerResponse post(String url, FormBody param) {
        return collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut).post(url, param);
    }

    @Override
    public SpinnerResponse post(String url, String jsonStrData) {
        return collectTemplate.create(readTimeOut,connectTimeOut,writeTimeOut).post(url, jsonStrData);
    }
}
