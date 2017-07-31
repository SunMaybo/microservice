package com.juxinli;

import com.juxinli.spinner.CollectTemplate;
import com.juxinli.spinner.HttpCollectTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by maybo on 17/7/17.
 */
@Configuration
public class AppConfiguration {

    @Bean
    public CollectTemplate httpCollectTemplate(){
        return  new HttpCollectTemplate().create(120,60,60);
    }
}
