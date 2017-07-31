package com.juxinli;

import com.juxinli.common.JsonObjectMapper;
import com.juxinli.common.Message;
import com.juxinli.rabbit.JRabbitTemplate;
import com.juxinli.spinner.ResponseProcessor;
import com.juxinli.spinner.SpinnerEngine;
import com.juxinli.template.JRestTemplate;
import okhttp3.FormBody;
import okhttp3.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@SpringBootApplication
@RestController
//@EnableEurekaClient
public class Application {

	@Autowired
	JRabbitTemplate jRabbitTemplate;

	@Autowired
	ValueOperations<String,Object>valueOperations;

	@Autowired
	private SpinnerEngine spinnerEngine;

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;

private Logger logger = Logger.getLogger(this.getClass());


	@RequestMapping("/")
	 public String test(String mobile,String password){
		redisTemplate.delete(mobile);
		Map<String,Object> dataMap =new HashMap<String,Object>();
		dataMap.put("name","maybo");
		dataMap.put("age", 34);
		Message msg=new Message();
		msg.setStatus(200);
		msg.setObject(dataMap);
		FormBody.Builder builder=new FormBody.Builder();
		builder.add("mobile", LianTongAuthUtil.encrypt(mobile));
		builder.add("password",LianTongAuthUtil.encrypt(password));
		builder.add("deviceId","c92046624200b63600dfeff23eec0763120d8ba8a1c6e07525a134022f9c6f33");
		builder.add("netWay","Over");
		builder.add("deviceOS","10.3.1");
		builder.add("deviceCode", "06974ABF-0E1A-4955-B90F-A27A9630B5A1");
		builder.add("deviceBrand", "iphone");
		builder.add("version", "iphone_c@5.2");
		builder.add("deviceModel", "iPhone");
		spinnerEngine.post("http://m.client.10010.com/mobileService/login.htm", builder.build(), 1000 * 60 * 10l, mobile, new ResponseProcessor() {
			@Override
			public void process(Response response) {
				if (response.code()==200){
					String reStr= null;
					try {
						reStr = response.body().string();
						if (reStr.contains("\"code\":\"0\"")){
							String url="http://m.client.10010.com/mobileService/query/getSmsCodePhone.htm";
							Response response1=	spinnerEngine.post(url, 1000 * 60 * 10l, mobile);
							logger.info(response1.body().string());
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					logger.info(reStr);
				}
			}
		});
		return "调用成功!";
	}
	@RequestMapping("/sendSms")
	public String testfind(String mobile , String code){
		FormBody.Builder builder=new FormBody.Builder();
		builder.add("smsCodeRes", code);
		spinnerEngine.post("http://m.client.10010.com/mobileService/login.htm",builder.build() , 1000 * 60 * 10l, mobile, new ResponseProcessor() {
			@Override
			public void process(Response response) {
				if (response.code() == 200) {
					String reStr = null;
					try {
						reStr = response.body().string();

					} catch (IOException e) {
						e.printStackTrace();
					}
					logger.info(reStr);
				}
			}
		});
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
