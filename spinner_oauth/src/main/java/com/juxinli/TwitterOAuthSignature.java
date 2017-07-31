package com.juxinli;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by maybo on 17/7/11.
 */
@Component
public class TwitterOAuthSignature {

    private static final String UTF8 = "UTF-8";

    @Value("${twitter.oauth1.0a.oauth.consumer.key}")
    private String oauth_consumer_key = "jOYDznWpH3uP4vhm8VIei8Ny8";

    private String oauth_nonce = nonce();

    private String oauth_signature_method = "HMAC-SHA1";

    private long oauth_timestamp = new Date().getTime() / 1000;

    @Value("${twitter.oauth1.0a.oauth.consumer.secret}")
    private String oauth_consumer_secret = "atl8I7LNaQcikxVPKot53qASztKqrUmQqOwhnhPWixlSfbasLj";


    private static String requestTokenUrl = "https://api.twitter.com/oauth/request_token";

    private String accessTokenUrl = "https://api.twitter.com/oauth/access_token";

    @Value("${twitter.oauth1.0a.callback}")
    private String call_back = "https://its.juxinli.com/ITS_statistic/queryString";

    private static String nonce() {
        return Base64Utils.encodeToString(UUID.randomUUID().toString().replace("-", "").getBytes()).replace("=", "");
    }

    private static String percentEncodeRfc3986(String s) {
        String out;
        try {
            out = URLEncoder.encode(s, UTF8).replace("+", "%20")
                    .replace("*", "%2A").replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            out = s;
        }
        return out;
    }

    public String token() throws UnsupportedEncodingException {
        String queryString = "";
        queryString += percentEncodeRfc3986("oauth_callback") + "=" + percentEncodeRfc3986(call_back) + "&";
        queryString += percentEncodeRfc3986("oauth_consumer_key") + "=" + percentEncodeRfc3986(oauth_consumer_key) + "&";
        queryString += percentEncodeRfc3986("oauth_nonce") + "=" + percentEncodeRfc3986(oauth_nonce) + "&";
        queryString += percentEncodeRfc3986("oauth_signature_method") + "=" + percentEncodeRfc3986(oauth_signature_method) + "&";
        queryString += percentEncodeRfc3986("oauth_timestamp") + "=" + percentEncodeRfc3986(oauth_timestamp + "") + "&";
        queryString += percentEncodeRfc3986("oauth_version") + "=" + percentEncodeRfc3986("1.0");
        queryString = "POST&" + percentEncodeRfc3986(requestTokenUrl) + "&" + percentEncodeRfc3986(queryString);
        String sign_secret = "";
        sign_secret = percentEncodeRfc3986(oauth_consumer_secret) + "&";
        try {
            String url = URLEncoder.encode(call_back, "UTF-8");
            String authorization = "OAuth ";
            authorization += "oauth_nonce =" + "\"" + URLEncoder.encode(oauth_nonce, "UTF-8") + "\"" + ",";
            authorization += "oauth_callback =" + "\"" + url + "\"" + ",";
            authorization += "oauth_signature_method = " + "\"" + URLEncoder.encode(oauth_signature_method, "UTF-8") + "\"" + ",";
            authorization += "oauth_timestamp = " + "\"" + URLEncoder.encode(oauth_timestamp + "", "UTF-8") + "\"" + ",";
            authorization += "oauth_consumer_key = " + "\"" + URLEncoder.encode(oauth_consumer_key, "UTF-8") + "\"" + ",";
            authorization += "oauth_signature = " + "\"" + URLEncoder.encode(Signature.hmacSHA1Base64Encode(queryString, sign_secret), "UTF-8") + "\"" + ",";
            authorization += "oauth_version = " + "\"" + URLEncoder.encode("1.0", "UTF-8") + "\"";
            return authorization;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String accessToken(String oauthToken, String tokenSecret) throws UnsupportedEncodingException {
        String queryString = "";
        queryString += percentEncodeRfc3986("oauth_consumer_key") + "=" + percentEncodeRfc3986(oauth_consumer_key) + "&";
        queryString += percentEncodeRfc3986("oauth_nonce") + "=" + percentEncodeRfc3986(oauth_nonce) + "&";
        queryString += percentEncodeRfc3986("oauth_signature_method") + "=" + percentEncodeRfc3986(oauth_signature_method) + "&";
        queryString += percentEncodeRfc3986("oauth_timestamp") + "=" + percentEncodeRfc3986(oauth_timestamp + "") + "&";
        queryString += percentEncodeRfc3986("oauth_token") + "=" + percentEncodeRfc3986(oauthToken) + "&";
        queryString += percentEncodeRfc3986("oauth_version") + "=" + percentEncodeRfc3986("1.0");

        queryString = "POST&" + percentEncodeRfc3986(accessTokenUrl) + "&" + percentEncodeRfc3986(queryString);
        String sign_secret = "";
        sign_secret = percentEncodeRfc3986(oauth_consumer_secret) + "&" + percentEncodeRfc3986(tokenSecret);
        try {

            String authorization = "OAuth ";
            authorization += "oauth_nonce =" + "\"" + URLEncoder.encode(oauth_nonce, "UTF-8") + "\"" + ",";
            authorization += "oauth_signature_method = " + "\"" + URLEncoder.encode(oauth_signature_method, "UTF-8") + "\"" + ",";
            authorization += "oauth_timestamp = " + "\"" + URLEncoder.encode(oauth_timestamp + "", "UTF-8") + "\"" + ",";
            authorization += "oauth_consumer_key = " + "\"" + URLEncoder.encode(oauth_consumer_key, "UTF-8") + "\"" + ",";
            authorization += "oauth_token = " + "\"" + URLEncoder.encode(oauthToken, "UTF-8") + "\"" + ",";
            authorization += "oauth_signature = " + "\"" + URLEncoder.encode(Signature.hmacSHA1Base64Encode(queryString, sign_secret), "UTF-8") + "\"" + ",";
            authorization += "oauth_version = " + "\"" + URLEncoder.encode("1.0", "UTF-8") + "\"";
            return authorization;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String apiToken(Map<String, String> params,String tokenSecret,String url,String httpMethod) {
        String oauthToken = params.get("oauth_token");
        params.put("oauth_consumer_key", oauth_consumer_key);
        params.put("oauth_nonce", oauth_nonce);
        params.put("oauth_signature_method", oauth_signature_method);
        params.put("oauth_timestamp", oauth_timestamp + "");
        params.put("oauth_version", "1.0");
        String queryString = "";
        TreeMap<String, String> treeMap = new TreeMap<String, String>(new Comparator<String>() {

            /*
             * int compare(Object o1, Object o2) 返回一个基本类型的整型，
             * 返回负数表示：o1 小于o2，
             * 返回0 表示：o1和o2相等，
             * 返回正数表示：o1大于o2。
             */
            public int compare(String o1, String o2) {

                //指定排序器按照降序排列
                return o1.compareTo(o2);
            }
        });
        treeMap.putAll(params);
        Iterator iterator = treeMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            queryString += percentEncodeRfc3986(key) + "=" + percentEncodeRfc3986(treeMap.get(key)) + "&";
        }
        queryString = queryString.substring(0, queryString.length() - 1);

        System.out.println(queryString);

        queryString = httpMethod + "&" + percentEncodeRfc3986(url) + "&" + percentEncodeRfc3986(queryString);
        String sign_secret = "";
        sign_secret = percentEncodeRfc3986(oauth_consumer_secret) + "&" + percentEncodeRfc3986(tokenSecret);
        try {

            String authorization = "OAuth ";
            authorization += "oauth_nonce =" + "\"" + URLEncoder.encode(oauth_nonce, "UTF-8") + "\"" + ",";
            authorization += "oauth_signature_method = " + "\"" + URLEncoder.encode(oauth_signature_method, "UTF-8") + "\"" + ",";
            authorization += "oauth_timestamp = " + "\"" + URLEncoder.encode(oauth_timestamp + "", "UTF-8") + "\"" + ",";
            authorization += "oauth_consumer_key = " + "\"" + URLEncoder.encode(oauth_consumer_key, "UTF-8") + "\"" + ",";
            authorization += "oauth_token = " + "\"" + URLEncoder.encode(oauthToken, "UTF-8") + "\"" + ",";
            authorization += "oauth_signature = " + "\"" + URLEncoder.encode(Signature.hmacSHA1Base64Encode(queryString, sign_secret), "UTF-8") + "\"" + ",";
            authorization += "oauth_version = " + "\"" + URLEncoder.encode("1.0", "UTF-8") + "\"";
            return authorization;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String apiToken(String oauthToken, String tokenSecret, String url, String httpMethod) throws UnsupportedEncodingException {
        String queryString = "";
        queryString += percentEncodeRfc3986("oauth_consumer_key") + "=" + percentEncodeRfc3986(oauth_consumer_key) + "&";
        queryString += percentEncodeRfc3986("oauth_nonce") + "=" + percentEncodeRfc3986(oauth_nonce) + "&";
        queryString += percentEncodeRfc3986("oauth_signature_method") + "=" + percentEncodeRfc3986(oauth_signature_method) + "&";
        queryString += percentEncodeRfc3986("oauth_timestamp") + "=" + percentEncodeRfc3986(oauth_timestamp + "") + "&";
        queryString += percentEncodeRfc3986("oauth_token") + "=" + percentEncodeRfc3986(oauthToken) + "&";
        queryString += percentEncodeRfc3986("oauth_version") + "=" + percentEncodeRfc3986("1.0");

        queryString = httpMethod + "&" + percentEncodeRfc3986(url) + "&" + percentEncodeRfc3986(queryString);
        String sign_secret = "";
        sign_secret = percentEncodeRfc3986(oauth_consumer_secret) + "&" + percentEncodeRfc3986(tokenSecret);
        try {

            String authorization = "OAuth ";
            authorization += "oauth_nonce =" + "\"" + URLEncoder.encode(oauth_nonce, "UTF-8") + "\"" + ",";
            authorization += "oauth_signature_method = " + "\"" + URLEncoder.encode(oauth_signature_method, "UTF-8") + "\"" + ",";
            authorization += "oauth_timestamp = " + "\"" + URLEncoder.encode(oauth_timestamp + "", "UTF-8") + "\"" + ",";
            authorization += "oauth_consumer_key = " + "\"" + URLEncoder.encode(oauth_consumer_key, "UTF-8") + "\"" + ",";
            authorization += "oauth_token = " + "\"" + URLEncoder.encode(oauthToken, "UTF-8") + "\"" + ",";
            authorization += "oauth_signature = " + "\"" + URLEncoder.encode(Signature.hmacSHA1Base64Encode(queryString, sign_secret), "UTF-8") + "\"" + ",";
            authorization += "oauth_version = " + "\"" + URLEncoder.encode("1.0", "UTF-8") + "\"";
            return authorization;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Map<String,String>dataMap=new HashMap<>();
        dataMap.put("oauth_token","880315414659706881-q6DwKjGQMFEKrwikejZdBQEGW3idmkx");
        dataMap.put("screen_name","Ethan930147677");
        dataMap.put("count",5000+"");
        System.out.println((new TwitterOAuthSignature()).apiToken(dataMap, "cU8TkampNQ0Z3VisnuIMLkCXny49k8JlSEXxDCf1l3oH7","https://api.twitter.com/1.1/followers/ids.json",
                "GET"));
    }

}
