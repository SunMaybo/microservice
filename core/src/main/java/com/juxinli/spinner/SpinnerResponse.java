package com.juxinli.spinner;

/**
 * Created by maybo on 17/7/19.
 */
public class SpinnerResponse {
    private int code;
    private String data;
    private Exception exception;
    private String description;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SpinnerResponse{" +
                "code=" + code +
                ", data='" + data + '\'' +
                ", exception=" + exception +
                ", description='" + description + '\'' +
                '}';
    }
}
