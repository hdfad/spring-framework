package com.xwj.aware;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/05/30 15:16
 */
@Component
public class ApplicationContextHolder implements ApplicationContextAware{
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextHolder.applicationContext=applicationContext;
	}

	public static Object getBean(String beanName){
		return applicationContext.getBean(beanName);
	}
}
