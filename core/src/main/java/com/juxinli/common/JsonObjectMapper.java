package com.juxinli.common;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.PropertyNamingStrategy;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Set;

/**
 * Json与java实体见转换，包括JSR-303数据校验
 * Created by maybo on 17/7/27.
 */
public class JsonObjectMapper<T> {

    private static final Logger LOGGER = Logger.getLogger(JsonObjectMapper.class);

    private static ObjectMapper objectMapper = null;

    private static Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public JsonObjectMapper() {
    }


    public T readerValueAsObject(String jsonString, Class clazz) {
        try {
            if (null == jsonString) {
                return null;
            } else {
                jsonString = jsonString.trim();
            }
            if ("".equals(jsonString)) {
                return null;
            }
            return (T) JsonObjectMapper.getDefaultObjectMapper().readValue(jsonString, clazz);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
    public String writeValueAsString(T t) throws IOException {

        return JsonObjectMapper.getDefaultObjectMapper().writeValueAsString(t);

    }

    public ValidObject<T> readerValueAsValidObject(String jsonString, Class clazz) {
        if (null == jsonString) {
            return null;
        } else {
            jsonString = jsonString.trim();
            if ("".equals(jsonString)) {
                return null;
            }
        }
        ValidObject<T> validObject = new ValidObject<T>();
        try {
            T t = (T) JsonObjectMapper.getDefaultObjectMapper().readValue(jsonString, clazz);
            Set<ConstraintViolation<T>> violations = validator.validate(t);
            if (violations.size() <= 0) {
                validObject.setIsSuccess(true);
                validObject.setData(t);
            } else {
                StringBuffer buf = new StringBuffer();
                for (ConstraintViolation<T> violation : violations) {
                    buf.append(violation.getMessage() + ",");
                }
                buf.deleteCharAt(buf.length() - 1);
                validObject.setIsSuccess(false);
                validObject.setMessage(buf.toString());
            }
            return validObject;
        } catch (IOException e) {
            e.printStackTrace();
            validObject.setIsSuccess(false);
            validObject.setException(e);
            validObject.setMessage(e.getMessage());
            return validObject;
        }

    }

    private static synchronized ObjectMapper getDefaultObjectMapper() {

        if (objectMapper == null) {
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
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
            //设置有属性不能映射成PO时不报错
            objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
            return objectMapper;
        } else {
            return objectMapper;
        }
    }

    public static void main(String[] args) {


    }

}
