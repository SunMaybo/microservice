package com.juxinli;

import com.juxinli.common.Message;
import com.juxinli.rabbit.JRabbitListener;
import com.juxinli.rabbit.ReceiveMessageListener;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Created by maybo on 17/6/26.
 */
@JRabbitListener(queues = "queue")
@Component
public class ReceiveService implements ReceiveMessageListener {

    private Logger logger = Logger.getLogger(this.getClass());
    @Override
    public void process(Message message) {
        logger.info("-------------------收到消息来自队列:queue--------------------");
        logger.info(message.toString());
    }
}
