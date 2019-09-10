package com.wjwcloud.iot.utils;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public SpringContextUtils() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    public static void addBean(String beanName, Class beanClass) {
        BeanDefinitionRegistry beanDefReg = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
        if (!beanDefReg.containsBeanDefinition(beanName)) {
            BeanDefinitionBuilder beanDefBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass);
            BeanDefinition beanDef = beanDefBuilder.getBeanDefinition();
            beanDefReg.registerBeanDefinition(beanName, beanDef);
        }

    }

    public static void addBean(String beanName, String classPath) {
        BeanDefinitionRegistry beanDefReg = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
        BeanDefinitionBuilder beanDefBuilder = BeanDefinitionBuilder.genericBeanDefinition(classPath);
        BeanDefinition beanDef = beanDefBuilder.getBeanDefinition();
        if (!beanDefReg.containsBeanDefinition(beanName)) {
            beanDefReg.registerBeanDefinition(beanName, beanDef);
        }

    }

    public static void removeBean(String beanName) {
        BeanDefinitionRegistry beanDefReg = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
        beanDefReg.getBeanDefinition(beanName);
        beanDefReg.removeBeanDefinition(beanName);
    }
}
