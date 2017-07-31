package com.juxinli.rabbit;

import com.juxinli.spring.SpringBeanDefinitionRegistry;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

import java.util.List;

/**
 * Created by maybo on 17/6/23.
 */

public class Binding {

    private Logger logger = Logger.getLogger(this.getClass());

    private BindingProperties bindingProperties;
    private ConnectionFactory connectionFactory;
    private SpringBeanDefinitionRegistry springBeanDefinitionRegistry;
    private UniqueRandomCodeComponet uniqueRandomCodeComponet;

    public Binding(BindingProperties bindingProperties, ConnectionFactory connectionFactory,SpringBeanDefinitionRegistry springBeanDefinitionRegistry,UniqueRandomCodeComponet uniqueRandomCodeComponet) {
        this.bindingProperties = bindingProperties;
        this.connectionFactory = connectionFactory;
        this.springBeanDefinitionRegistry=springBeanDefinitionRegistry;
        this.uniqueRandomCodeComponet=uniqueRandomCodeComponet;
        init();
    }

    private void init() {
        RabbitAdmin admin = new RabbitAdmin(this.connectionFactory);
        List<Exchange> exchanges = bindingProperties.getExchanges();
        if (null == exchanges) {
            return;
        }

        for (Exchange exchange : exchanges) {
            String name = exchange.getName();
            Schema schema = exchange.getSchema();
            if (null == schema) {
                continue;
            }
            if (schema.getFanout()!=null&&schema.getTopics()!=null){
                logger.error("同一个交换机下不能设置两种模式");
                System.exit(1);
            }
            fanoutExchange(name, schema.getFanout(), admin);
            topicExchange(name, schema.getTopics(), admin);
        }
    }

    private void fanoutExchange(String name, Fanout fanout, RabbitAdmin rabbitAdmin) {
        if (null == fanout) {
            return;
        }
        FanoutExchange fanoutExchange = new FanoutExchange(name);
        rabbitAdmin.declareExchange(fanoutExchange);
        if (fanout.isAutoCreateQueue()){
            Queue queue=new Queue(uniqueRandomCodeComponet.getRandomCode());
            springBeanDefinitionRegistry.registry(uniqueRandomCodeComponet.getRandomCode(),queue);
            rabbitAdmin.declareQueue(queue);
            rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(fanoutExchange));
            logger.info("广播模式绑定消息队列:" + "交换机:" + name + ",队列名字:" + uniqueRandomCodeComponet.getRandomCode());
        }
        String queues = fanout.getQueues();
        if (null == queues) {
            return;
        }
        for (String queueName : queues.split(",")) {
            Queue queue=new Queue(queueName);
            springBeanDefinitionRegistry.registry(queueName,queue);
            rabbitAdmin.declareQueue(queue);
            rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(fanoutExchange));
            logger.info("广播模式绑定消息队列:" + "交换机:" + name + ",队列名字:" + queueName);

        }

    }

    private void topicExchange(String name, List<Topic> topics, RabbitAdmin rabbitAdmin) {

        if (null == topics) {
            return;
        }
        TopicExchange topicExchange = new TopicExchange(name);
        rabbitAdmin.declareExchange(topicExchange);
        for (Topic topic : topics) {
            String topicName = topic.getName();
            String queues = topic.getQueues();
            if (null == queues) {
                break;
            }
            for (String queueName : queues.split(",")) {
                Queue queue=new Queue(queueName);
                springBeanDefinitionRegistry.registry(queueName, queue);
                rabbitAdmin.declareQueue(queue);
                rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(topicExchange).with(topicName));
                logger.info("主题模式绑定消息队列:" + "交换机:" + name + ",主题名字:" + topicName + ",队列名字:" + queueName);
            }
        }
    }

}
