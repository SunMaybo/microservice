package com.juxinli.rabbit;

import com.juxinli.util.UUIDUtil;

import java.lang.annotation.*;

/**
 * Created by maybo on 17/5/10.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JRabbitListener {

    public static final String TOPIC_EXCHANGE="topic";

    public static final String FANOUT_EXCHANGE="fanout";

    public static final String RANDOM_CODE= UUIDUtil.getRandomString();

    boolean isAutoCreateQueue() default false;

    String exchangeType() default "topic";

    String[] queues() default {};
}

