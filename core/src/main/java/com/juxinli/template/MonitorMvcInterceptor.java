package com.juxinli.template;

import com.juxinli.common.Indicator;
import com.juxinli.common.JsonObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by maybo on 17/6/20.
 */
@Configuration
public class MonitorMvcInterceptor extends WebMvcConfigurerAdapter implements HandlerInterceptor {

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private Environment environment;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(this).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        String traceId=request.getHeader("traceCode");
        Indicator indicator=null;
        if (null==traceId){
             indicator =new Indicator();
        }else {
             indicator=new Indicator(traceId);
        }
        indicator.setDepth(indicator.getDepth() + 1);
        String name =environment.getProperty("spring.application.name");
        indicator.setTime(new Date().getTime());
        indicator.getExtend().put("url", request.getRequestURI());
        indicator.setName(name);
        indicator.getExtend().put("remoteHost", request.getRemoteHost());
        indicator.getExtend().put("remoteAddr",request.getRemoteAddr());
        indicator.getExtend().put("contextPath",request.getContextPath());
        indicator.getExtend().put("remoteUser", request.getRemoteUser());
        indicator.getExtend().put("protocol", request.getProtocol());
        indicator.setType(Indicator.HTTP_MVC_PRE);
        JsonObjectMapper<Indicator>jsonObjectMapper=new JsonObjectMapper<>();
        logger.info(jsonObjectMapper.writeValueAsString(indicator));
        request.setAttribute("traceCode",indicator.traceCode());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {



    }
}
