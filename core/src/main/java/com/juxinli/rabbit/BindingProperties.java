package com.juxinli.rabbit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maybo on 17/6/23.
 */
@ConfigurationProperties(prefix = "spring.rabbitmq.binding")
@Component
public class BindingProperties {


    private List<Exchange> exchanges=new ArrayList<>();



    public void setExchanges(List<Exchange> exchanges) {
        this.exchanges = exchanges;
    }

    public List<Exchange> getExchanges() {
        return exchanges;
    }

}
