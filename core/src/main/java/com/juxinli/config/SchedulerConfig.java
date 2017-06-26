package com.juxinli.config;

import com.juxinli.spring.AutowiringSpringBeanJobFactory;
import com.juxinli.spring.SpringClassScanUtils;
import com.juxinli.quartz.QuartzDataSourceProperties;
import com.juxinli.quartz.QuartzScheduled;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Configuration
@ConditionalOnProperty(name = "quartz.enabled")
public class SchedulerConfig {

    @Autowired
    private QuartzDataSourceProperties quartzDataSourceProperties;

    private Logger logger= Logger.getLogger(this.getClass());
    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }


    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setOverwriteExistingJobs(true);
        if (null!=getDataSource()) {
            factory.setQuartzProperties(quartzProperties());
            factory.setDataSource(getDataSource());
        }
        factory.setJobFactory(jobFactory);
        Trigger[] triggers = loadTrigger();
        factory.setTriggers(triggers);

        return factory;
    }
private DataSource getDataSource(){
    if (quartzDataSourceProperties.isDatasourceEnabled()){
        BasicDataSource basicDataSource =new BasicDataSource();
        basicDataSource.setUrl(quartzDataSourceProperties.getUrl());
        basicDataSource.setUsername(quartzDataSourceProperties.getUsername());
        basicDataSource.setPassword(quartzDataSourceProperties.getPassword());
        basicDataSource.setDriverClassName(quartzDataSourceProperties.getDriverClassName());
        return basicDataSource;
    }
    return null;
}
private Trigger[] loadTrigger(){
    List<Class<?>> classList=getQuartScheduled();
    Trigger[] triggers=new Trigger[classList.size()];
   for (int i=0;i<classList.size();i++){
       QuartzScheduled quartzScheduled=classList.get(i).getAnnotation(QuartzScheduled.class);
                if (quartzScheduled.cron().equals("")&&quartzScheduled.fixRate()>0){
                    triggers[i]=((Trigger) createTrigger(classList.get(i), quartzScheduled.fixRate(),quartzScheduled.text()));
                }
                if (!quartzScheduled.cron().equals("")){
                    triggers[i]=(Trigger) createCronTrigger(classList.get(i), quartzScheduled.cron(),quartzScheduled.text());
                }


    }
    return triggers;
}
    private List<Class<?>>getQuartScheduled(){
        List<Class<?>>classList= SpringClassScanUtils.scan();
      List<Class<?>>classArrayList=new ArrayList<>();
        if (null!=classList&&classList.size()>0){
            for (Class<?> clazz:classList){
                QuartzScheduled quartzScheduled=clazz.getAnnotation(QuartzScheduled.class);
                if (null!=quartzScheduled){
                    classArrayList.add(clazz);
                }
            }
        }
        return classArrayList;
    }
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
       ClassPathResource classPathResource= new ClassPathResource("/quartz.properties");
        if (classPathResource.exists()) {
            propertiesFactoryBean.setLocation(classPathResource);
        }
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    public static CronTrigger createCronTrigger(Class jobClass,String cron,String text){
        CronTrigger cronTrigger=createCronTrigger((JobDetail) createJobDetail(jobClass,text), cron);
        return cronTrigger;
    }

    public  static SimpleTrigger createTrigger(Class jobClass, long pollFrequencyM,String text){
       return createTrigger((JobDetail)createJobDetail(jobClass,text),pollFrequencyM);
    }

    public static JobDetail createJobDetail(Class jobClass,String text) {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        // job has to be durable to be stored in DB:
        factoryBean.setDurability(true);
        factoryBean.setName(jobClass.getName() + ".job");
        factoryBean.setGroup("juxinli_job");
        factoryBean.setDescription(text);
        factoryBean.afterPropertiesSet();
        JobDetail jobDetail=factoryBean.getObject();

        return jobDetail;
    }

    private static SimpleTrigger createTrigger(JobDetail jobDetail, long pollFrequencyMs) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setStartDelay(0L);
        factoryBean.setRepeatInterval(pollFrequencyMs);
        factoryBean.setGroup("juxinli_trigger");
        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        // in case of misfire, ignore all missed triggers and continue :
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
        factoryBean.setName(jobDetail.getJobClass().getName() + ".trigger");
        factoryBean.afterPropertiesSet();
        SimpleTrigger simpleTrigger=factoryBean.getObject();
        return simpleTrigger;
    }

    // Use this method for creating cron triggers instead of simple triggers:
    public static CronTrigger createCronTrigger(JobDetail jobDetail, String cronExpression) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setGroup("juxinli_trigger");
        factoryBean.setCronExpression(cronExpression);
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        factoryBean.setName(jobDetail.getJobClass().getName() + ".trigger");
        try {
            factoryBean.afterPropertiesSet();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return factoryBean.getObject();
    }

}
