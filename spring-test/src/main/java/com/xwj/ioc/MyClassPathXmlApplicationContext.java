package com.xwj.ioc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2023/02/14 16:53
 */
public class MyClassPathXmlApplicationContext extends ClassPathXmlApplicationContext {
	public MyClassPathXmlApplicationContext(String... configLocations) throws BeansException {
		super(configLocations);
	}

	@Override
	protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
		beanFactory.setAllowCircularReferences(false);
	}
}
