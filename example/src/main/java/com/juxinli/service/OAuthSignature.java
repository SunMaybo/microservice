package com.juxinli.service;

import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * Created by maybo on 17/7/11.
 */
public class OAuthSignature {


    private static final String UTF8 = "UTF-8";
    private static String status = "Hello Ladies + Gentlemen, a signed OAuth request!";

    private static boolean include_entities = true;

    private static String oauth_consumer_key = "jOYDznWpH3uP4vhm8VIei8Ny8";

    private static String oauth_nonce = nonce();

    private static String oauth_signature_method = "HMAC-SHA1";

    private static long oauth_timestamp = new Date().getTime() / 1000;

    private static String oauth_consumer_secert = "atl8I7LNaQcikxVPKot53qASztKqrUmQqOwhnhPWixlSfbasLj";

    private static String token_secert = "3UhLzTg7YsWyob1IT7KJy9xVSjzkyCP5";

    private static String oauth_token = "V6Ff9wAAAAAA1XqdAAABXTW7N9w";


    private static String nonce() {
        return Base64Utils.encodeToString(UUID.randomUUID().toString().replace("-", "").getBytes()).replace("=", "");
    }

    //"include_entities =真oauth_consumer_key = xvz1evFS4wEEPTGEFPHBog＆oauth_nonce = kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg＆oauth_signature_method = HMAC-SHA1＆oauth_timestamp = 1318622958＆组oauth_token = 370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9we​​JAEb＆oauth_version = 1.0＆状态=你好％20Ladies％20％2B％20Gentlemen％2C％20A％20signed％20OAuth％20request％21"
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

    public static String token() throws UnsupportedEncodingException {
        String queryString = "";
        String url = URLEncoder.encode("https://its.juxinli.com/ITS_statistic/queryString", "UTF-8");
        queryString+=percentEncodeRfc3986("oauth_callback")+"="+percentEncodeRfc3986("https://its.juxinli.com/ITS_statistic/queryString")+"&";
        queryString += percentEncodeRfc3986("oauth_consumer_key") + "=" + percentEncodeRfc3986(oauth_consumer_key) + "&";
        queryString += percentEncodeRfc3986("oauth_nonce") + "=" + percentEncodeRfc3986(oauth_nonce) + "&";
        queryString += percentEncodeRfc3986("oauth_signature_method") + "=" + percentEncodeRfc3986(oauth_signature_method) + "&";
        queryString += percentEncodeRfc3986("oauth_timestamp") + "=" + percentEncodeRfc3986(oauth_timestamp + "") + "&";
       // queryString += percentEncodeRfc3986("oauth_token") + "=" + percentEncodeRfc3986(oauth_token) + "&";
        //queryString+=percentEncodeRfc3986("oauth_verifier")+"="+percentEncodeRfc3986("9099778")+"&";
        queryString += percentEncodeRfc3986("oauth_version") + "=" + percentEncodeRfc3986("1.0");
        String tokenUrl = "https://api.twitter.com/oauth/request_token";
        queryString = "POST&" + percentEncodeRfc3986(tokenUrl) + "&" + percentEncodeRfc3986(queryString);
        String sign_secret = "";
        sign_secret = percentEncodeRfc3986(oauth_consumer_secert) + "&";

        System.out.println(queryString);
        System.out.println(sign_secret);

        System.out.println(Signature.hmacSHA1Base64Encode(queryString, sign_secret));


        try {

            String authorization = "OAuth ";
            authorization += "oauth_nonce =" + "\"" + URLEncoder.encode(oauth_nonce, "UTF-8") + "\"" + ",";
            authorization+="oauth_callback ="+"\""+url+"\""+",";
            authorization += "oauth_signature_method = " + "\"" + URLEncoder.encode(oauth_signature_method, "UTF-8") + "\"" + ",";
            authorization += "oauth_timestamp = " + "\"" + URLEncoder.encode(oauth_timestamp + "", "UTF-8") + "\"" + ",";
            authorization += "oauth_consumer_key = " + "\"" + URLEncoder.encode(oauth_consumer_key, "UTF-8") + "\"" + ",";
          //  authorization += "oauth_token = " + "\"" + URLEncoder.encode(oauth_token, "UTF-8") + "\"" + ",";
            authorization += "oauth_signature = " + "\"" + URLEncoder.encode(Signature.hmacSHA1Base64Encode(queryString, sign_secret), "UTF-8") + "\"" + ",";
            authorization += "oauth_version = " + "\"" + URLEncoder.encode("1.0", "UTF-8") + "\"";
            System.out.println(authorization);
            return  authorization;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    return null;
    }
public static String accessToken() throws UnsupportedEncodingException {
    String queryString="";
    String url= URLEncoder.encode("https://its.juxinli.com/ITS_statistic/queryString", "UTF-8");
    //queryString+=percentEncodeRfc3986("oauth_callback")+"="+percentEncodeRfc3986("https://its.juxinli.com/ITS_statistic/queryString")+"&";
    queryString+=percentEncodeRfc3986("oauth_consumer_key")+"="+percentEncodeRfc3986(oauth_consumer_key)+"&";
    queryString+=percentEncodeRfc3986("oauth_nonce")+"="+percentEncodeRfc3986(oauth_nonce)+"&";
    queryString+=percentEncodeRfc3986("oauth_signature_method")+"="+percentEncodeRfc3986(oauth_signature_method)+"&";
    queryString+=percentEncodeRfc3986("oauth_timestamp")+"="+percentEncodeRfc3986(oauth_timestamp+"")+"&";
    queryString+=percentEncodeRfc3986("oauth_token")+"="+percentEncodeRfc3986(oauth_token)+"&";
    //queryString+=percentEncodeRfc3986("oauth_verifier")+"="+percentEncodeRfc3986("9099778")+"&";
    queryString+=percentEncodeRfc3986("oauth_version")+"="+percentEncodeRfc3986("1.0");
    String tokenUrl="https://api.twitter.com/oauth/access_token";
    queryString="POST&"+percentEncodeRfc3986(tokenUrl)+"&"+percentEncodeRfc3986(queryString);
    String sign_secret="";
    sign_secret=percentEncodeRfc3986(oauth_consumer_secert)+"&"+percentEncodeRfc3986(token_secert);

    System.out.println(queryString);
    System.out.println(sign_secret);

    System.out.println(Signature.hmacSHA1Base64Encode(queryString, sign_secret));


    try {

        String authorization="OAuth ";
        authorization+="oauth_nonce ="+"\""+URLEncoder.encode(oauth_nonce,"UTF-8")+"\""+",";
        //authorization+="oauth_callback ="+"\""+url+"\""+",";
        authorization+="oauth_signature_method = "+"\""+URLEncoder.encode(oauth_signature_method,"UTF-8") +"\""+",";
        authorization+="oauth_timestamp = "+"\""+URLEncoder.encode(oauth_timestamp+"","UTF-8") +"\""+",";
        authorization+="oauth_consumer_key = "+"\""+URLEncoder.encode(oauth_consumer_key,"UTF-8") +"\""+",";
        authorization+="oauth_token = "+"\""+URLEncoder.encode(oauth_token,"UTF-8") +"\""+",";
        authorization+="oauth_signature = "+"\""+URLEncoder.encode(Signature.hmacSHA1Base64Encode(queryString, sign_secret),"UTF-8") +"\""+",";
        authorization+="oauth_version = "+"\""+URLEncoder.encode("1.0","UTF-8") +"\"";
       return  authorization;
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
      return null;
}

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(token());
        System.out.println("---------");
        System.out.println(accessToken());
    }
}
