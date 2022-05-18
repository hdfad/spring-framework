package com.xwj.resolveBeforeInstantiation_parse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/05/18 11:35
 */
public class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
	@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		return new MyResolveBeforeInstantiationConfig();
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		MyResolveBeforeInstantiationConfig myResolveBeforeInstantiationConfig=(MyResolveBeforeInstantiationConfig)bean;
		myResolveBeforeInstantiationConfig.age=100;
		return myResolveBeforeInstantiationConfig;
	}
}
