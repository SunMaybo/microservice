package com.juxinli.rabbit;

import com.juxinli.common.Message;

/**
 * Created by maybo on 17/6/25.
 */
public interface ReceiveMessageListener {

    public void process(Message message);

}
