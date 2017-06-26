package com.juxinli.config;

import com.juxinli.common.Indicator;
import com.juxinli.common.JsonObjectMapper;
import com.juxinli.common.Message;
import com.juxinli.rabbit.*;
import com.juxinli.spring.SpringBeanDefinitionRegistry;
import com.juxinli.util.UUIDUtil;
import com.rabbitmq.client.Channel;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by maybo on 17/6/23.
 */
@Configuration
public class AmqpConfig {
    @Value("${spring.rabbitmq.addresses}")
    private String addresses;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.virtualHost}")
    private String virtualHost;
    @Value("${spring.rabbitmq.publisherConfirms:false}")
    private boolean publisherConfirms;

    @Autowired
    private SpringBeanDefinitionRegistry springBeanDefinitionRegistry;

    private Logger logger = Logger.getLogger(this.getClass());

    @Bean
    public ConnectionFactory connectionFactory(ApplicationContext applicationContext) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(addresses);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setApplicationContext(applicationContext);
        //** 如果要进行消息回调，则这里必须要设置为true *//*
        connectionFactory.setPublisherConfirms(publisherConfirms);
        return connectionFactory;
    }


    //** 因为要设置回调类，所以应是prototype类型，如果是singleton类型，则回调类为最后一次设置 *//*
    //@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean
    public RabbitTemplate rabbitTemplate(@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        return template;
    }


    @Bean
    public JRabbitTemplate jRabbitTemplate(@Qualifier("rabbitTemplate") RabbitTemplate rabbitTemplate, @Qualifier("bindingProperties") BindingProperties bindingProperties, @Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
        binding(bindingProperties, connectionFactory);
        messageContainer(connectionFactory);
        return new JRabbitTemplate(rabbitTemplate);
    }

    public void messageContainer(ConnectionFactory connectionFactory) {
       final List<Object> objectList = springBeanDefinitionRegistry.getBeansOfAnnotation(JRabbitListener.class);
        for (int i = 0; i < objectList.size(); i++) {
            JRabbitListener jRabbitListener = objectList.get(i).getClass().getAnnotation(JRabbitListener.class);
            String[] queueNames = jRabbitListener.queues();
            if (jRabbitListener.isAutoCreateQueue()){//随机队列
              if (null!=queueNames){
                  queueNames= Arrays.copyOf(queueNames, queueNames.length + 1);
                  queueNames[queueNames.length-1]=JRabbitListener.RANDOM_CODE;
              }else {
                  queueNames=new String[1];
                  queueNames[0]= UUIDUtil.getOrderIdByUUId();
              }
            }
            if (null != queueNames) {
                Queue[] queues = new Queue[queueNames.length];
                for (int j = 0; j < queueNames.length; j++) {
                    Queue queue = (Queue) springBeanDefinitionRegistry.getBean(queueNames[j]);
                    if (queue != null) {
                        queues[j] = queue;
                    } else {
                        return;
                    }
                }
                final int index = i;
                SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
                container.setQueues(queues);
                container.setExposeListenerChannel(true);
                container.setMaxConcurrentConsumers(1);
                container.setConcurrentConsumers(1);
                container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认
                container.setMessageListener(new ChannelAwareMessageListener() {

                    @Override
                    public void onMessage(org.springframework.amqp.core.Message message, Channel channel) throws Exception {
                        byte[] body = message.getBody();
                        Message msg = null;
                        try {
                            msg = (Message) JsonObjectMapper.readerValueAsObject(new String(body, "utf-8"), Message.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String traceCode = null;
                        if (msg != null) {
                            traceCode = msg.getTraceCode();
                        }
                        try {
                            Indicator indicator = null;
                            if (null == traceCode) {
                                indicator = new Indicator();
                            } else {
                                indicator = new Indicator(traceCode);
                            }
                            indicator.setStatus(Indicator.SUCCESS);
                            long start = new Date().getTime();
                            ReceiveMessageListener receiveMessageListener = (ReceiveMessageListener) objectList.get(index);
                            receiveMessageListener.process(msg);
                            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //确认消息成功消费
                            long end = new Date().getTime();
                            indicator.getExtend().put("spendTime", (end - start));
                            indicator.setType(Indicator.RABBIT_TEMPLATE);
                            indicator.setDepth(indicator.getDepth()+1);
                            logger.info(JsonObjectMapper.writeValueAsString(indicator));
                        } catch (Exception e) {
                            Indicator indicator = new Indicator(traceCode);
                            indicator.setStatus(Indicator.FAIL);
                            indicator.setType(Indicator.RABBIT_TEMPLATE);
                            e.printStackTrace();
                            indicator.setError(e.getMessage());
                            logger.error(JsonObjectMapper.writeValueAsString(indicator), e);
                        }

                    }
                });
                springBeanDefinitionRegistry.registry("container" + "_" + (i + 1), container);
            }
        }

    }

    private void binding(BindingProperties bindingProperties, ConnectionFactory connectionFactory) {
        new Binding(bindingProperties, connectionFactory, springBeanDefinitionRegistry);

    }

}
