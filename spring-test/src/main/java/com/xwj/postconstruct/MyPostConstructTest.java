package com.xwj.postconstruct;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class MyPostConstructTest implements InitializingBean, DisposableBean {
	@Override
	public void destroy() throws Exception {
		System.out.println();
		System.out.println("destroy");
		System.out.println();

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println();
		System.out.println("afterPropertiesSet");
		System.out.println();
	}

	@PostConstruct
	public void  postConstructxwj(){
		System.out.println();
		System.out.println("postConstruct");
		System.out.println();
	}

	@PreDestroy
	public void  preDestroy(){
		System.out.println();
		System.out.println("postConstruct");
		System.out.println();

	}

	public void init(){
		System.out.println();
		System.out.println("通过配置文件的init-method的xml配置");
		System.out.println();
	}
	public void  cleanUp(){
		System.out.println();
		System.out.println("通过配置文件的destroy-method的xml配置");
		System.out.println();
	}
}
