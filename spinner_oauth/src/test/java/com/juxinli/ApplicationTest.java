package com.juxinli;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by sunchong on 2017/3/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ApplicationTest {

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    TwitterOauthServiceImpl twitterOauthService;

    @Test
   public void twitterRequestToken(){

        logger.info(twitterOauthService.requestToken());

   }

}
