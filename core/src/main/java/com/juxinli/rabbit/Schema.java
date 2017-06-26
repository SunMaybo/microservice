package com.juxinli.rabbit;

import java.util.List;

/**
 * Created by maybo on 17/6/23.
 */
public class Schema {
    private List<Topic> topics;
    private Fanout fanout;

    public void setFanout(Fanout fanout) {
        this.fanout = fanout;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public Fanout getFanout() {
        return fanout;
    }

    public List<Topic> getTopics() {
        return topics;
    }
}
