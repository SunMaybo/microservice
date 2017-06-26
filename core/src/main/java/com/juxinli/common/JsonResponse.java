package com.juxinli.common;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.log4j.Logger;

import java.lang.*;


/**
 * Created by maybo on 2016/10/20.
 */
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class JsonResponse<T>{

    private static Logger logger = Logger.getLogger(JsonResponse.class);

    private String token;

    private int code;

    private String codeDescription;
    private T data;


    public JsonResponse(){

    }

    public static JsonResponse createError(String message,Throwable e){
        JsonResponse jsonResponse=new JsonResponse<>();
        jsonResponse.setCode(500);
        jsonResponse.setCodeDescription(message);
        logger.debug(e.getMessage());
        return jsonResponse;
    }
    public static JsonResponse createData(Object data){
        JsonResponse jsonResponse=new JsonResponse<>();
        jsonResponse.setCode(200);
        jsonResponse.setCodeDescription("调用成功");
        jsonResponse.setData(data);
        return jsonResponse;
    }
    public  JsonResponse data(T data){

        this.code = 200;
        this.codeDescription = "调用成功";
        this.data = data;
        return this;
    }
    public JsonResponse(T data){
        this.code = 200;
        this.codeDescription = "调用成功";
        this.data = data;
    }

    public static JsonResponse  createError(Error error){

        JsonResponse jsonResponse = new JsonResponse();

        jsonResponse.setCode(500);

        String codeDescription=error.getCodeDescription();

        jsonResponse.setCodeDescription(codeDescription);

        logger.debug(error.toString());
        return jsonResponse;

    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public  JsonResponse  error(Error error){

        this.code = error.getCode();

        this.codeDescription = error.getCodeDescription();

        logger.debug(error.toString());
        return this;

    }
    public JsonResponse error(String message,Throwable e){

        this.code=500;
        this.codeDescription=message;
        logger.error(e.getMessage());
        return this;
    }
    public int getCode(){
        return code;
    }

    public void setCode(int code){
        this.code = code;
    }

    public String getCodeDescription(){
        return codeDescription;
    }

    public void setCodeDescription(String codeDescription){
        this.codeDescription = codeDescription;
    }

    public T getData(){
        return data;
    }

    public void setData(T data){
        this.data = data;
    }

}
