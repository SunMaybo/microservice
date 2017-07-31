package com.juxinli.redis;

import com.juxinli.common.JsonObjectMapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by maybo on 17/6/28.
 */
public class JsonRedisObjectSerializer implements RedisSerializer<Object> {

    @Override
    public byte[] serialize(Object o) throws SerializationException {

        if (o==null){
            return null;
        }
        try {
            return JsonObjectMapper.writeValueAsString(o).getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        try {
            if (null==bytes){
                return null;
            }
            String string =new String(bytes,"utf-8");
            return JsonObjectMapper.readerValueAsObject(new String(bytes,"utf-8"),Object.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
       return null;
    }
}
