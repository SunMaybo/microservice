package com.juxinli.service;

import org.springframework.util.Base64Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.SignatureException;

/**
 * Created by maybo on 17/7/11.
 */
public class Signature {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    public static String hmacSHA1Base64Encode(String data,String key){
        try {
            return Base64Utils.encodeToString(hmacSHA1(data, key));
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return  null;
    }
    public static byte[] hmacSHA1(String data, String key) throws java.security.SignatureException {
        try {
            // get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(),
                    HMAC_SHA1_ALGORITHM);

            // get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);

            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes());

            return rawHmac;

        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : "
                    + e.getMessage());
        }
    }
}
