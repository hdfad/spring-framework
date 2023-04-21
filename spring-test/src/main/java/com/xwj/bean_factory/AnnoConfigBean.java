package com.xwj.bean_factory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class AnnoConfigBean implements InitializingBean, DisposableBean {
	@Override
	public void destroy() throws Exception {
		System.out.println();
		System.out.println("Component-destroy");
		System.out.println();

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println();
		System.out.println("Component-afterPropertiesSet");
		System.out.println();
	}

	@PostConstruct
	public void  postConstruct(){
		System.out.println();
		System.out.println("Component-postConstruct");
		System.out.println();
	}

	@PreDestroy
	public void  preDestroy(){
		System.out.println();
		System.out.println("Component-postConstruct");
		System.out.println();

	}
}