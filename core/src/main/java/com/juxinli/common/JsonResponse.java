package com.juxinli.common;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.log4j.Logger;

import java.lang.*;


/**
 * 带状态的返回对象
 * Created by maybo on 2016/10/20.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class JsonResponse<T> {

    private static Logger logger = Logger.getLogger(JsonResponse.class);

    private String token;

    private String code;

    private int status;

    private String codeDescription;

    private T data;


    private String errorDescription;


    public JsonResponse() {

    }

    public static JsonResponse createError(String message, Throwable e) {
        JsonResponse jsonResponse = new JsonResponse<>();
        jsonResponse.setCode("FAIL");
        jsonResponse.setStatus(5000);
        jsonResponse.setCodeDescription(message);
        if (e != null) {
            logger.debug("message:" + message + "," + "Exception:" + e.getMessage());
        } else {
            logger.debug("message:" + message);
        }
        logger.debug(e.getMessage());
        return jsonResponse;
    }

    public static JsonResponse createError(String message, Throwable e, int status) {
        JsonResponse jsonResponse = new JsonResponse<>();
        jsonResponse.setCode("FAIL");
        jsonResponse.setStatus(status);
        jsonResponse.setCodeDescription(message);
        if (e != null) {
            logger.debug("message:" + message + "," + "Exception:" + e.getMessage() + "state:" + status);
        } else {
            logger.debug("message:" + message + "," + "status:" + status);
        }
        return jsonResponse;
    }

    public static JsonResponse createData(Object data) {
        JsonResponse jsonResponse = new JsonResponse<>();
        jsonResponse.setCode("FAIL");
        jsonResponse.setCodeDescription("调用成功");
        jsonResponse.setData(data);
        return jsonResponse;
    }

    public JsonResponse data(T data) {

        this.code = "SUCCESS";
        this.codeDescription = "调用成功";
        this.data = data;
        return this;
    }

    public JsonResponse(T data) {
        this.code = "SUCCESS";
        this.codeDescription = "调用成功";
        this.data = data;
    }

    public static JsonResponse createError(Error error) {

        JsonResponse jsonResponse = new JsonResponse();

        jsonResponse.setCode("FAIL");
        jsonResponse.setStatus(error.getStatus());

        String codeDescription = error.getDescription();

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

    public JsonResponse error(Error error) {

        this.status = error.getStatus();
        this.code = "FAIL";

        this.codeDescription = error.getDescription();

        logger.debug(error.toString());
        return this;

    }

    public JsonResponse error(String message, Throwable e) {

        this.code = "FAIL";
        this.status = 5000;
        this.codeDescription = message;
        if (e != null) {
            logger.debug("message:" + message + "," + "Exception:" + e.getMessage());
        } else {
            logger.debug("message:" + message);
        }
        logger.error(e.getMessage());
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getCodeDescription() {
        return codeDescription;
    }

    public void setCodeDescription(String codeDescription) {
        this.codeDescription = codeDescription;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
