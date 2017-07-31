package com.juxinli.config;

import com.juxinli.spinner.HttpCookieCollectTemplate;
import com.juxinli.spinner.SpinnerEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by maybo on 17/6/28.
 */
@Configuration
public class SpinnerConfig {

    @Bean
    public SpinnerEngine spinnerEngine(RedisTemplate<String,Object> redisTemplate){

        return new SpinnerEngine(new HttpCookieCollectTemplate(),redisTemplate);

    }
}
