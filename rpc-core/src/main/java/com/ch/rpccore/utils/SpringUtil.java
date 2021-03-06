package com.ch.rpccore.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Description: TODO
 * @Author chenhao
 * @Date 2020/3/13
 **/
@Component
public class SpringUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    public static Object getBean(String name){
        return applicationContext.getBean(name);
    }

    public <T> T getBean(String name, Class<T> clazz){
        return applicationContext.getBean(name, clazz);
    }

    public <T> T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }
}
