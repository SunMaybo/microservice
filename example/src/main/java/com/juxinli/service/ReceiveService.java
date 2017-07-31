package com.juxinli.service;

import com.juxinli.common.Message;
import com.juxinli.rabbit.JRabbitListener;
import com.juxinli.rabbit.ReceiveMessageListener;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Created by maybo on 17/6/26.
 */
@Component
@JRabbitListener(isAutoCreateQueue = true)
public class ReceiveService implements ReceiveMessageListener {
    private Logger logger = Logger.getLogger(this.getClass());
    @Override
    public void process(Message message) {
        logger.info("---------------这是一条全局广播接受的消息-------------");
        logger.info(message.toString());
    }
}
