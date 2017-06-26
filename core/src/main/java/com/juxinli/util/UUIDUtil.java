package com.juxinli.util;

import java.util.UUID;

/**
 * Created by maybo on 17/6/20.
 */
public class UUIDUtil {
    public static String getOrderIdByUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }

    public static String getRandomString(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
