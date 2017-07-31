package com.juxinli;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by maybo on 17/5/24.
 */
public class LianTongAuthUtil {

    private static final Logger LOGGER=Logger.getLogger(LianTongAuthUtil.class);

    private static String randomString = "000000";

    private static Random random = new Random();


    public static String encrypt(String paramString){
        randomString = (random.nextInt(9) + "" + random.nextInt(9) + "" + random.nextInt(9) + "" + random.nextInt(9) + "" + random.nextInt(9) + "" + random.nextInt(9));
        String encryptStr= encode(paramString+randomString);
        LOGGER.info("密文:"+encryptStr);
        return encryptStr;
    }
    private static String encode(String paramString) {
        return HexEncrypt.encode(AESEncrypt.encode(paramString.getBytes(), HexEncrypt.decode("f6b0d3f905bf02939b4f6d29f257c2ab"), HexEncrypt.decode("1a42eb4565be8628a807403d67dce78d")));
    }
    private static class AESEncrypt
    {
        private static SecureRandom a = new SecureRandom();

        public static byte[] encode(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
        {
            return encode(paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3, 1);
        }

        private static byte[] encode(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt)
        {
            try
            {
                SecretKeySpec localSecretKeySpec = new SecretKeySpec(paramArrayOfByte2, "AES");
                IvParameterSpec localIvParameterSpec = new IvParameterSpec(paramArrayOfByte3);
                Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                localCipher.init(paramInt, localSecretKeySpec, localIvParameterSpec);
                byte[] arrayOfByte = localCipher.doFinal(paramArrayOfByte1);
                return arrayOfByte;
            }
            catch (GeneralSecurityException localGeneralSecurityException) {
                LOGGER.error("AES加密失败",localGeneralSecurityException);
            }
            return null;
        }
    }

    private static class HexEncrypt
    {
        public static String encode(byte[] paramArrayOfByte)
        {
            return Hex.encodeHexString(paramArrayOfByte);
        }

        public static byte[] decode(String paramString)
        {
            try
            {
                byte[] arrayOfByte = Hex.decodeHex(paramString.toCharArray());
                return arrayOfByte;
            } catch (DecoderException e) {
               LOGGER.error("Hex解析异常",e);
            }
            return null;
        }
    }

}
