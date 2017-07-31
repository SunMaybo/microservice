package com.juxinli.rabbit;

import com.juxinli.common.*;
import com.juxinli.common.Message;
import com.juxinli.common.Indicator;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.*;

import java.io.IOException;
import java.util.Date;

/**
 * Created by maybo on 17/6/23.
 */

public class JRabbitTemplate {

    private Logger logger = Logger.getLogger(this.getClass());

    private AmqpTemplate amqpTemplate;

    public JRabbitTemplate(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void convertAndSend(String exchange, String routeKey, com.juxinli.common.Message message) {
        if (exchange == null) {
            throw new NullPointerException("交换机不可以为空!");
        }
        Indicator indicator = null;
        if (null == message) {
            logger.warn("发送的消息为空!");
            indicator = new Indicator();
            message = new Message();
            message.setTraceCode(indicator.traceCode());
        }
        if (message.getTraceCode() == null) {
            indicator = new Indicator();
            message.setTraceCode(indicator.traceCode());
        } else {
            indicator = new Indicator(message.getTraceCode());
        }
        indicator.setType(Indicator.RABBIT_TEMPLATE);
        try {
            long start = new Date().getTime();
            amqpTemplate.convertAndSend(exchange, routeKey, JsonObjectMapper.writeValueAsString(message));
            long end = new Date().getTime();
            indicator.getExtend().put("spendTime", (long) (end - start));
            logger.info(JsonObjectMapper.writeValueAsString(indicator));
        } catch (Exception e) {
            indicator.setError(e.getMessage());
            indicator.setStatus(Indicator.FAIL);
            indicator.setType(Indicator.RABBIT_TEMPLATE);
            try {
                logger.error(JsonObjectMapper.writeValueAsString(indicator), e);
            } catch (IOException e1) {
                e1.printStackTrace();
                logger.error("实体转化为json字符串异常!", e1);
            }
        }


    }

    public void convertAndSend(String exchange, String routeKey, Message message, Indicator indicator) {
        if (exchange == null || indicator == null) {
            throw new NullPointerException("exchange or indicator传入参数不可以为空!");
        }
        if (null == message) {
            logger.error("发送的消息为空!");
            throw new NullPointerException("发送消息不可以为空!");
        }
        indicator = new Indicator(message.getTraceCode());
        indicator.setType(Indicator.RABBIT_TEMPLATE);
        try {
            long start = new Date().getTime();
            amqpTemplate.convertAndSend(exchange, routeKey, JsonObjectMapper.writeValueAsString(message));
            long end = new Date().getTime();
            indicator.getExtend().put("spendTime", (long) (end - start));
            logger.info(JsonObjectMapper.writeValueAsString(indicator));
        } catch (Exception e) {
            indicator.setError(e.getMessage());
            indicator.setStatus(Indicator.FAIL);
            try {
                logger.error(JsonObjectMapper.writeValueAsString(indicator), e);
            } catch (IOException e1) {
                e1.printStackTrace();
                logger.error("实体转化为json字符串异常!", e1);
            }
        }


    }
}
