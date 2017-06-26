package com.juxinli.rabbit;

/**
 * Created by maybo on 17/6/23.
 */
public class Fanout {
    private String queues;

    private boolean autoCreateQueue=false;

    public void setAutoCreateQueue(boolean autoCreateQueue) {
        this.autoCreateQueue = autoCreateQueue;
    }

    public boolean isAutoCreateQueue() {
        return autoCreateQueue;
    }

    public void setQueues(String queues) {
        this.queues = queues;
    }

    public String getQueues() {
        return queues;
    }
}
