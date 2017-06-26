package com.juxinli.rabbit;

/**
 * Created by maybo on 17/6/23.
 */
public class Topic {
    private String name;
    private String queues;
    public void setName(String name) {
        this.name = name;
    }

    public void setQueues(String queues) {
        this.queues = queues;
    }

    public String getName() {
        return name;
    }

    public String getQueues() {
        return queues;
    }
}
