package com.juxinli.quartz;

import java.lang.annotation.*;

/**
 * Created by maybo on 17/5/10.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QuartzScheduled {
    String cron() default "";
    long fixRate() default 0 ;
    String text() default "";
}
