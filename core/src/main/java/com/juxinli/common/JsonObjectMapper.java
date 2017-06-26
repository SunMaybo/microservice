package com.juxinli.common;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by maybo on 17/6/20.
 */
public class JsonObjectMapper {

    private static  ObjectMapper objectMapper=null;

    private JsonObjectMapper(){}

    public static String writeValueAsString(Object object){
        try {
            return JsonObjectMapper.getDefaultObjectMapper().writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object readerValueAsObject(String jsonString,Class clazz){
        try {
           return JsonObjectMapper.getDefaultObjectMapper().readValue(jsonString,clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static synchronized ObjectMapper getDefaultObjectMapper() {

            if (objectMapper==null){
                objectMapper = new ObjectMapper();
                //设置将对象转换成JSON字符串时候:包含的属性不能为空或"";
                //Include.Include.ALWAYS 默认
                //Include.NON_DEFAULT 属性为默认值不序列化
                //Include.NON_EMPTY 属性为 空（""）  或者为 NULL 都不序列化
                //Include.NON_NULL 属性为NULL 不序列化
                objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
                //设置将MAP转换为JSON时候只转换值不等于NULL的
                objectMapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
                objectMapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
                objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                //设置有属性不能映射成PO时不报错
                objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
                return objectMapper;
            }else {
                return  objectMapper;
            }
        }




}
