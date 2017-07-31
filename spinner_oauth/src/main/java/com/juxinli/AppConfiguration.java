package com.juxinli;

import com.juxinli.spinner.CollectTemplate;
import com.juxinli.spinner.HttpCollectTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by maybo on 17/7/17.
 */
@Configuration
public class AppConfiguration {

    @Bean
    public CollectTemplate httpCollectTemplate(){
        return  new HttpCollectTemplate().create(10,10,10);
    }
}
