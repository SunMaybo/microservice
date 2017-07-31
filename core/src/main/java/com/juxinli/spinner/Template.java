package com.juxinli.spinner;

import okhttp3.*;

/**
 * Created by maybo on 17/6/28.
 */
public interface Template {

    public SpinnerResponse get(String url);

    public SpinnerResponse get(String url, Headers headers);

    public SpinnerResponse post(String url, String jsonStrData, Headers headers);


    public SpinnerResponse post(String url, FormBody param, Headers headers);

    public SpinnerResponse post(String url, Headers headers);

    public SpinnerResponse post(String url);

    public SpinnerResponse post(String url, FormBody param);

    public SpinnerResponse post(String url, String jsonStrData);
}