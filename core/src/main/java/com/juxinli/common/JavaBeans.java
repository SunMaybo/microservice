package com.juxinli.common;

import org.springframework.beans.BeanUtils;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * Created by maybo on 2017/7/31.
 */
public class JavaBeans {
    public static void copyProperties(Object src, Object des) {
        BeanUtils.copyProperties(src, des);
    }
    
    public static ValidObject<Object> copyValidProperties(Object src, Object des) {
        ValidObject<Object> validObject = new ValidObject<>();
        BeanUtils.copyProperties(src, des);
        return ValidObject.validObject(des);
    }
}
