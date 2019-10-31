package com.wjwcloud.ad.system.helper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * BeanHelper
 */
@Component
public class BeanHelper implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    /**
     * 通过名称获取bean
     *
     * @param name
     * @return
     */
    public Object getBean(String name){
        return applicationContext.getBean(name);
    }
}
