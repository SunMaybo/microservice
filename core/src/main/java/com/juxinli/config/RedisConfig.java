package com.juxinli.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juxinli.redis.JsonRedisObjectSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by maybo on 17/6/28.
 */
@Configuration
public class RedisConfig {

    /*@Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }*/

    @Bean
    ValueOperations<String, String> strOperations(RedisTemplate<String, String> stringRedisTemplate) {
        return stringRedisTemplate.opsForValue();
    }

    @Bean
    RedisTemplate<String, Integer> intRedisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<String, Integer>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    ValueOperations<String, Integer> intOperations(RedisTemplate<String, Integer> intRedisTemplate) {
        return intRedisTemplate.opsForValue();
    }


    @Bean
    RedisTemplate<String, Object> objRedisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        StringRedisSerializer stringRedisSerializer =new StringRedisSerializer();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setValueSerializer(new JsonRedisObjectSerializer());
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(new JsonRedisObjectSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    ValueOperations<String, Object> objOperations(RedisTemplate<String, Object> objRedisTemplate) {
        return objRedisTemplate.opsForValue();
    }

}