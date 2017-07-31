package com.juxinli.template;

import com.juxinli.common.GlobalError;
import com.juxinli.common.Indicator;
import com.juxinli.common.JsonObjectMapper;
import com.juxinli.common.ValidObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Created by maybo on 2017/7/31.
 */
@Component
@ConditionalOnProperty("loadBalanced.enabled")
public class JRestValidTemplate {
    
    private Logger logger = Logger.getLogger(this.getClass());
    
    
    @Autowired
    private RestTemplate restTemplate;
    
    
    public <T> ValidObject<T> postForObject(String url, Object request, Class<T> responseType, Object... uriVariables) {
        
        return postForObject(url, request, responseType, null, uriVariables);
        
    }
    
    
    public <T> ValidObject<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) {
        
        
        return exchange(url, method, requestEntity, responseType, null, uriVariables);
        
        
    }
    
    
    public <T> ValidObject<T> postForObject(String url, Object request, Class<T> responseType, Indicator indicator, Object... uriVariables) {
        
        if (null == indicator) {
            indicator = new Indicator();
        }
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("traceCode", indicator.traceCode());
        HttpEntity<?> stringHttpEntity = new HttpEntity<>(request, headers);
        return exchange(url, HttpMethod.POST, stringHttpEntity, responseType, indicator, uriVariables);
        
    }
    
    public <T> ValidObject<T> getForObject(String url, Class<T> responseType, Object... uriVariables) {
        return getForObject(url, responseType, null, uriVariables);
    }
    
    public <T> ValidObject<T> getForObject(String url, Class<T> responseType, Indicator indicator, Object... uriVariables) {
        if (null == indicator) {
            indicator = new Indicator();
        }
        
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("traceCode", indicator.traceCode());
        HttpEntity<?> stringHttpEntity = new HttpEntity<>(headers);
        return exchange(url, HttpMethod.GET, stringHttpEntity, responseType, indicator, uriVariables);
        
    }
    
    public <T> ValidObject<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Indicator indicator, Object... uriVariables) {
        
        
        ResponseEntity<T> t = null;
        
        if (null == indicator) {
            indicator = new Indicator();
        }
        
        Map<String, Object> dataMap = indicator.getExtend();
        
        dataMap.put("url", url);
        dataMap.put("httpMethod", method.name());
        
        ValidObject<T> validObject = new ValidObject<>();
        
        try {
            
            Date start = new Date();
            
            t = restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
            int status = t.getStatusCodeValue();
            if (status >= 500) {
                validObject.setIsSuccess(false);
                validObject.setError(GlobalError.REMOTE_METHOD_EXCEPTION);
                validObject.setMessage("Http:调用接口返回的状态码:" + status);
            } else if (status >= 400 && status < 500) {
                validObject.setIsSuccess(false);
                validObject.setError(GlobalError.CALL_REMOTE_FORMAT_EXCEPTION);
                validObject.setMessage("Http:调用接口返回的状态码:" + status);
            } else if (status >= 200 && status < 300) {
    
                /*
                这里需要对结果进行校验校验规则是:SRS-303
                 */
                validObject = ValidObject.validObject(t.getBody());
                validObject.setMessage("Http:调用接口返回的状态码:" + status);
                
                Date end = new Date();
                
                dataMap.put("spendTime", (long) (end.getTime() - start.getTime()));
                
                indicator.setType(Indicator.REST_TEMPLATE_TYPE);
                
                indicator.setStatus(status);
                logger.info(JsonObjectMapper.writeValueAsString(indicator));
                
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
            validObject.setIsSuccess(false);
            validObject.setException(e);
            validObject.setMessage("OkHttpClient客服端异常");
            indicator.setType(Indicator.REST_TEMPLATE_TYPE);
            indicator.setError(e.getMessage());
            indicator.setStatus(Indicator.FAIL);
            try {
                logger.error(JsonObjectMapper.writeValueAsString(indicator), e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        
        return validObject;
        
    }
    
}