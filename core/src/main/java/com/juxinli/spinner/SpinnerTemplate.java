package com.juxinli.spinner;

import okhttp3.FormBody;
import okhttp3.Headers;

/**
 * Created by maybo on 17/6/28.
 */
public interface SpinnerTemplate extends Template {

    public SpinnerResponse get(String url,ResponseProcessor responseProcessor);

    public SpinnerResponse get(String url, Headers headers,ResponseProcessor responseProcessor);

    public SpinnerResponse post(String url, String jsonStrData, Headers headers,ResponseProcessor responseProcessor);

    public SpinnerResponse post(String url, FormBody param, Headers headers,ResponseProcessor responseProcessor);

    public SpinnerResponse post(String url, Headers headers,ResponseProcessor responseProcessor);

    public SpinnerResponse post(String url,ResponseProcessor responseProcessor);

    public SpinnerResponse post(String url, FormBody param,ResponseProcessor responseProcessor);

    public SpinnerResponse post(String url, String jsonStrData,ResponseProcessor responseProcessor);

    public SpinnerResponse get(String url,long cookieTimeOut,String cookieCode,ResponseProcessor responseProcessor);

    public SpinnerResponse get(String url, Headers headers,long cookieTimeOut,String cookieCode,ResponseProcessor responseProcessor);

    public SpinnerResponse post(String url, String jsonStrData, Headers headers,long cookieTimeOut,String cookieCode,ResponseProcessor responseProcessor);

    public SpinnerResponse post(String url, FormBody param, Headers headers,long cookieTimeOut,String cookieCode,ResponseProcessor responseProcessor);

    public SpinnerResponse post(String url, Headers headers,long cookieTimeOut,String cookieCode,ResponseProcessor responseProcessor);

    public SpinnerResponse post(String url,long cookieTimeOut,String cookieCode,ResponseProcessor responseProcessor);

    public SpinnerResponse post(String url, FormBody param,long cookieTimeOut,String cookieCode,ResponseProcessor responseProcessor);

    public SpinnerResponse post(String url, String jsonStrData,long cookieTimeOut,String cookieCode,ResponseProcessor responseProcessor);


    public SpinnerResponse get(String url,long cookieTimeOut,String cookieCode);

    public SpinnerResponse get(String url, Headers headers,long cookieTimeOut,String cookieCode);

    public SpinnerResponse post(String url, String jsonStrData, Headers headers,long cookieTimeOut,String cookieCode);


    public SpinnerResponse post(String url, FormBody param, Headers headers,long cookieTimeOut,String cookieCode);

    public SpinnerResponse post(String url, Headers headers,long cookieTimeOut,String cookieCode);

    public SpinnerResponse post(String url,long cookieTimeOut,String cookieCode);

    public SpinnerResponse post(String url, FormBody param,long cookieTimeOut,String cookieCode);

    public SpinnerResponse post(String url, String jsonStrData,long cookieTimeOut,String cookieCode);
}
