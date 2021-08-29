package com.xwj.ioc;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Component;

//@Component
public class MyBeanNameAware implements BeanNameAware {
	@Override
	public void setBeanName(String name) {
		System.out.println(name);
	}
}
