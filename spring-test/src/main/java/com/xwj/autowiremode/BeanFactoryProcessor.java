package com.xwj.autowiremode;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.stereotype.Component;

/**
 * 实现{@link BeanFactoryPostProcessor}接口获取Bean的定义信息
 * 修改Bean的装配模型
 * @Author: Raphael
 */
@Component
public class BeanFactoryProcessor implements BeanFactoryPostProcessor {

    private static final String BEAN_NAME = "consumer";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
            throws BeansException {
        AbstractBeanDefinition beanDefinition = (AbstractBeanDefinition)beanFactory.getBeanDefinition(BEAN_NAME);
        beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
    }

}
