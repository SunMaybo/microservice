package com.juxinli.template;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by maybo on 17/6/20.
 */
@Configuration
@ConditionalOnProperty("loadBalanced.enabled")
public class AppRestTemplateConfig {

    @Value("${rest.connection.request.timeout: 3000}")
    private int restTemplateConnectionRequestTimeout;
    @Value("${rest.connection.timeout: 3000}")
    private int restTemplateConnectionTimeout;
    @Value("${rest.read.timeout: 3000}")
    private int restTemplateReadTimeout;

    @Bean@LoadBalanced
    public RestTemplate restTemplate(){

        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(restTemplateConnectionRequestTimeout);
        httpRequestFactory.setConnectTimeout(restTemplateConnectionTimeout);
        httpRequestFactory.setReadTimeout(restTemplateReadTimeout);
        return new RestTemplate(httpRequestFactory);
    }
}
