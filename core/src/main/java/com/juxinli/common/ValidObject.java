package com.juxinli.common;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * Created by maybo on 17/7/27.
 */
public class ValidObject<T> {
    
    private T data;
    
    private boolean isSuccess;
    
    private Error error;
    
    private String message;
    
    private Exception exception;
    
    public void setError(Error error) {
        this.error = error;
    }
    
    public Error getError() {
        return error;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public boolean isSuccess() {
        return isSuccess;
    }
    
    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public void setException(Exception exception) {
        this.exception = exception;
    }
    
    public Exception getException() {
        return exception;
    }
    
    public static <T> ValidObject<T> validObject(T t) {
        ValidObject<T> validObject = new ValidObject<>();
        if (null == t) {
            validObject.setIsSuccess(true);
            validObject.setData(t);
        } else {
            Set<ConstraintViolation<T>> violations = ValidatorUtils.getValidator().validate(t);
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
        }
        return validObject;
    }
}
