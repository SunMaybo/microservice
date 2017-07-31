package com.juxinli.common;

/**
 * Created by maybo on 17/5/8.
 */public interface   Error {


    String getDescription();


    int getStatus();

    void setStatus(int status);


    void setDescription(String description);

}
