package com.juxinli.quartz;

import java.lang.annotation.*;

/**
 * Created by maybo on 17/5/14.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BordService {

    String baseUrl() default "";
    long timeOut() default 3000;
    String mails() default "";
    String prefix() default "";
    String text() default "";
}
