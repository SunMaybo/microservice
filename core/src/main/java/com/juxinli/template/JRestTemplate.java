package com.juxinli.template;

import com.juxinli.common.Indicator;
import com.juxinli.common.JsonObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;

/**
 *
 * Created by maybo on 17/6/20.
 */
@Component@ConditionalOnProperty("loadBalanced.enabled")
public class JRestTemplate  {

    private Logger logger = Logger.getLogger(this.getClass());


    @Autowired
    private RestTemplate restTemplate;



    public <T> T postForObject(String url, Object request, Class<T> responseType, Object... uriVariables)  {

      return postForObject(url,request,responseType,null,uriVariables);

    }


    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables)  {


        return  exchange(url,method,requestEntity,responseType,null,uriVariables);


    }


    public <T> T postForObject(String url, Object request, Class<T> responseType, Indicator indicator, Object... uriVariables)  {

        if (null==indicator){
            indicator =new Indicator();
        }
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("traceCode",indicator.traceCode());
        HttpEntity<?> stringHttpEntity =new HttpEntity<>(request,headers);
        ResponseEntity<T> responseEntity= exchange(url,HttpMethod.POST,stringHttpEntity,responseType,indicator,uriVariables);
        return (T)responseEntity.getBody();

    }

    public <T> T getForObject(String url,Class<T> responseType, Object... uriVariables){
        return getForObject(url, responseType, null, uriVariables);
    }

    public <T> T getForObject(String url,Class<T> responseType, Indicator indicator ,Object... uriVariables){
        if (null==indicator){
            indicator =new Indicator();
        }

        HttpHeaders headers = new HttpHeaders();
                 MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
                 headers.setContentType(type);
                 headers.add("Accept", MediaType.APPLICATION_JSON.toString());
                 headers.add("traceCode",indicator.traceCode());
        HttpEntity<?> stringHttpEntity =new HttpEntity<>(headers);
        ResponseEntity<T> responseEntity= exchange(url,HttpMethod.GET,stringHttpEntity,responseType,indicator,uriVariables);
        return (T)responseEntity.getBody();

    }

    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Indicator indicator, Object... uriVariables)  {


        ResponseEntity<T> t= null;

        if (null==indicator){
            indicator =new Indicator();
        }

        Map<String,Object> dataMap = indicator.getExtend();

        dataMap.put("url",url);
        dataMap.put("httpMethod",method.name());

        try {

            Date start = new Date();

            t  = restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);

            Date end =new Date();

            dataMap.put("spendTime", (long) (end.getTime() - start.getTime()));

            indicator.setType(Indicator.REST_TEMPLATE_TYPE);

            int status = t.getStatusCodeValue();

            indicator.setStatus(status);

            logger.info(JsonObjectMapper.writeValueAsString(indicator));


        }catch (Exception e){
            e.printStackTrace();
            indicator.setType(Indicator.REST_TEMPLATE_TYPE);
            indicator.setError(e.getMessage());
            indicator.setStatus(Indicator.FAIL);
            logger.error(JsonObjectMapper.writeValueAsString(indicator),e);
        }

        return t;

    }

}
