package com.juxinli.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maybo on 17/6/23.
 */
@Component
public class SpringBeanDefinitionRegistry {

    @Autowired
    private ApplicationContext applicationContext;

    public void registry(String beanName,Object object) {
        //DefaultListableBeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        defaultListableBeanFactory.registerSingleton(beanName, object);
    }


    public Object getBean(String name){
        try {
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
            return defaultListableBeanFactory.getBean(name);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;

    }
    public List<Object>getBeansOfAnnotation(Class annotationClass){
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        String[] beanNames = defaultListableBeanFactory.getBeanNamesForAnnotation(annotationClass);
        if (null!=beanNames){
            List<Object>objectList =new ArrayList<>();
            for (String name:beanNames) {
              Object object=defaultListableBeanFactory.getBean(name);
                objectList.add(object);
            }
            return objectList;
        }
        return null;
    }
    public List<Object>getBeansOfBeanClass(Class clazz){
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        String[] beanNames = defaultListableBeanFactory.getBeanNamesForType(clazz);
        if (null!=beanNames){
            List<Object>objectList =new ArrayList<>();
            for (String name:beanNames) {
                Object object=defaultListableBeanFactory.getBean(name);
                objectList.add(object);
            }
            return objectList;
        }
        return null;
    }


}
