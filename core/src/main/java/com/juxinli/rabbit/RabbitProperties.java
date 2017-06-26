package com.juxinli.rabbit;

/**
 * Created by maybo on 17/6/23.
 */
public class RabbitProperties {

    private String virtualHost="/";
    /*
    rabbit:
    virtualhost: /
    binding:
       producer:
           type: topic
           exchange: cloud-stream-test
           queues: group_demo
       consumer:
           queues: group_demo

     */
}
