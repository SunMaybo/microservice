package com.juxinli.auth;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class EncryptUtil {

    /**
     * MD5 加密
     *
     * @param content
     * @param salt
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String MD5(String content, String salt) throws NoSuchAlgorithmException {
        MessageDigest instance = MessageDigest.getInstance("MD5");
        instance.update((content + "{" + salt.toString() + "}").getBytes(Charset.forName("UTF-8")));
        // 用来将字节转换成 16 进制表示的字符
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        // MD5 的计算结果是一个 128 位的长整数，用字节表示就是 16 个字节
        byte tmp[] = instance.digest();
        // 每个字节用 16 进制表示的话，使用两个字符，所以表示成 16 进制需要 32 个字符
        char str[] = new char[16 * 2];
        // 表示转换结果中对应的字符位置
        int k = 0;
        // 从第一个字节开始，对 MD5 的每一个字节转换成 16 进制字符的转换
        for (int i = 0; i < 16; i++) {
            // 取第 i 个字节
            byte byte0 = tmp[i];
            // 取字节中高 4 位的数字转换,>>> 为逻辑右移，将符号位一起右移
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            // 取字节中低 4 位的数字转换
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

    /**
     * AES 加密
     *
     * @param content    加密内容
     * @param encryptKey 加密秘钥
     * @return 加密数组
     * @throws Exception
     */
    public static byte[] aesEncrypt(String content, String encryptKey) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(encryptKey.getBytes("UTF-8"));
        keyGen.init(128, secureRandom);
        SecretKey secretKey = keyGen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");// 创建密码器
        cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
        byte[] result = cipher.doFinal(content.getBytes("UTF-8"));
        return result;
    }

    /**
     * AES 解密
     *
     * @param content    解密内容
     * @param encryptKey 解密秘钥
     * @return
     * @throws Exception
     */
    public static String aesDecrypt(byte[] content, String encryptKey) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(encryptKey.getBytes("UTF-8"));
        keyGen.init(128, secureRandom);
        SecretKey secretKey = keyGen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");// 创建密码器
        cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
        byte[] result = cipher.doFinal(content);
        return new String(result, "UTF-8");
    }


    /**
     * 获取AES加密后的 十六进制字符串
     *
     * @param content
     * @param encryptKey
     * @return
     * @throws Exception
     */
    public static String aesEncryptHexString(String content, String encryptKey) throws Exception {
        return parseByte2HexString(aesEncrypt(content, encryptKey));
    }


    /**
     * 解密AES加密后的 十六进制字符串
     *
     * @param hexContent
     * @param encryptKey
     * @return
     * @throws Exception
     */
    public static String aesDecryptHexString(String hexContent, String encryptKey) throws Exception {
        return aesDecrypt(parseHexStr2Byte(hexContent), encryptKey);
    }


    /**
     * 将二进制转换成16进制
     *
     * @param buffer
     * @return
     */
    public static String parseByte2HexString(byte buffer[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buffer.length; i++) {
            String hex = Integer.toHexString(buffer[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexString
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexString) {
        if (hexString.length() < 1)
            return null;
        byte[] result = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length() / 2; i++) {
            int high = Integer.parseInt(hexString.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexString.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static void main(String[] args){

        InputStreamReader is = new InputStreamReader(System.in); //new构造InputStreamReader对象
        BufferedReader br = new BufferedReader(is); //拿构造的方法传到BufferedReader中
        try{
            System.out.println("请输入密钥:");
            String key = br.readLine();
            if (null==key){
                System.out.println("密钥不可以为空");
                return;
            }
            System.out.println("请输入内容:");
            String text = br.readLine();
            if (null==text){
                System.out.println("内容不可以为空");
                return;
            }
            System.out.println("请输入选择1.加密，2.解密:");
            String choose = br.readLine();
            if ("1".equals(choose)){

             System.out.println(aesEncryptHexString(text, key));

            } else if ("2".equals(choose)){

                System.out.println(aesDecryptHexString(text, key));

            }else{
                System.out.println("错误的输入");
            }

        }
        catch(IOException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

