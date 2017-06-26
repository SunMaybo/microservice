package com.juxinli.common;

/**
 * Created by maybo on 17/5/8.
 */public interface   Error {


    String getCodeDescription();

    int getCode();

    void setCode(int code);

    void setCodeDescription(String codeDescription);

}
