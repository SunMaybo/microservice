package com.juxinli;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@SpringBootApplication
@RestController
@EnableEurekaClient
public class Application {


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
