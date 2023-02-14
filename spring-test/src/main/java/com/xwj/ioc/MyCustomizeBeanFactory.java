package com.xwj.ioc;

import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2023/02/14 16:53
 */
public class MyCustomizeBeanFactory extends ClassPathXmlApplicationContext {
	public MyCustomizeBeanFactory(String... configLocations) throws BeansException {
		super(configLocations);
	}
}
