package com.juxinli.common;

import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Created by maybo on 2017/7/31.
 */
@Configuration
public class ValidatorUtils {
    
    private static Validator validator;
    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    public static Validator getValidator() {
        return validator;
    }
}
