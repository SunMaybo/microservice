package com.juxinli.auth;

import org.apache.log4j.Logger;

import java.util.UUID;

/**
 * Created by maybo on 17/5/24.
 */
public class AuthUtil {

    private static final Logger LOGGER=Logger.getLogger(AuthUtil.class);



    private static final String SUFFIX_STR="sbchdbq6190_ftcc";

    public static final String KEY="c28881ee3cb73f7299243d7f6bf752ff";

    /*
    * 加密
    * 1.构造密钥生成器
    * 2.根据ecnodeRules规则初始化密钥生成器
    * 3.产生密钥
    * 4.创建和初始化密码器
    * 5.内容加密
    * 6.返回字符串
    */
    public static String encode(String encodeRules,String content) throws Exception {


          return   EncryptUtil.aesEncryptHexString(content,encodeRules);

    }
    /*
     * 解密
     * 解密过程：
     * 1.同加密1-4步
     * 2.将加密后的字符串反纺成byte[]数组
     * 3.将加密内容解密
     */
    public static String decode(String encodeRules,String content) throws Exception {
            return EncryptUtil.aesDecryptHexString(content, encodeRules);

    }

    public static String key(String content){
        content=content.replace(".","-");
        return UUID.nameUUIDFromBytes((SUFFIX_STR+"-"+content).getBytes()).toString().replace("-", "");

    }
    public static String key(){
        return UUID.randomUUID().toString().replace("-", "");

    }
    public static void main(String[] args) {

    }
}
