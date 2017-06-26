package com.juxinli.spring;
import com.juxinli.quartz.JService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maybo on 17/5/10.
 */
@Component
public class SpringClassScanUtils {

    private static final String RESOURCE_PATTERN = "/**/*.class";

    private static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

  private static Map<Class,Object>httpRestProxyBeanMap=new HashMap<>();

    public SpringClassScanUtils(){}
    public static   List<Class<?>> scan() {

        String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX+"com" + RESOURCE_PATTERN;
        List<Class<?>>classList=new ArrayList<>();

        try {
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader reader = readerFactory.getMetadataReader(resource);
                    String className = reader.getClassMetadata().getClassName();
                    Class<?> clazz=null;
                        try {
                            if (className.equals("ReportCount")){
                                continue;
                            }
                            clazz = Class.forName(className);
                        } catch (ClassNotFoundException e) {
                            continue;
                        }catch (ExceptionInInitializerError e){
                            continue;
                        }
                    JService jService = clazz.getAnnotation(JService.class);
                    if (null != jService) {
                        classList.add(clazz);
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classList;
    }

}