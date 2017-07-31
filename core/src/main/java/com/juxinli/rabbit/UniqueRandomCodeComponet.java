package com.juxinli.rabbit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Created by maybo on 17/6/27.
 */
@Component
public class UniqueRandomCodeComponet {

    @Autowired
    private Environment environment;

    private static Logger logger = Logger.getLogger(UniqueRandomCodeComponet.class);


    public String getRandomCode(){
        return getName()+"."+getIpAddress()+"."+getServerPort();
    }
    private String getName(){
        return environment.getProperty("spring.application.name",String.class);
    }
    private  int getServerPort(){
     return environment.getProperty("server.port",Integer.class);
    }

    private  String getIpAddress() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                } else {
                    if (netInterface.getName().contains("en4")||netInterface.getName().contains("eth")||netInterface.getName().contains("enp")) {
                        Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                        while (addresses.hasMoreElements()) {
                            ip = addresses.nextElement();
                            if (ip != null && ip instanceof Inet4Address) {
                                return ip.getHostAddress();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("IP地址获取失败", e);
        }
        return "";
    }

}

