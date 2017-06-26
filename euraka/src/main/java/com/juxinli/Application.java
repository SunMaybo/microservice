package com.juxinli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created by maybo on 17/6/21.
 */
@SpringBootApplication
@EnableEurekaServer
public class Application {
    public static void main(String[] args){
            SpringApplication.run(Application.class, args);
    }
}
