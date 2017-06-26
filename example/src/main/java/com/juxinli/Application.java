package com.juxinli;

import com.juxinli.common.Message;
import com.juxinli.rabbit.JRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@SpringBootApplication
@RestController
@EnableEurekaClient
public class Application {

	@Autowired
	JRabbitTemplate jRabbitTemplate;

	@RequestMapping("/")
	public String test(){
		Map<String,Object> dataMap =new HashMap<String,Object>();
		dataMap.put("name","maybo");
		dataMap.put("age", 34);
		Message msg=new Message();
		msg.setStatus(200);
		msg.setObject(dataMap);
		jRabbitTemplate.convertAndSend("topic_exchange","topic",msg);
		return "调用成功!";
	}
	@RequestMapping("/fanout")
	public String fanout(){
		Map<String,Object> dataMap =new HashMap<String,Object>();
		dataMap.put("des","全局广播");
		Message msg=new Message();
		msg.setStatus(200);
		msg.setObject(dataMap);
		jRabbitTemplate.convertAndSend("example",null,msg);
		return "OK";
	}
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
