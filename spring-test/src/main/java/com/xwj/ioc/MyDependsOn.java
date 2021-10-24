//package com.xwj.ioc;
//
//import org.springframework.beans.factory.FactoryBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//
//
//@Configuration
//public class MyDependsOn  implements FactoryBean<D> {
//	@Override
//	public D getObject() throws Exception {
//		return new D();
//	}
//
//	@Override
//	public Class<?> getObjectType() {
//		return D.class;
//	}
//
//
//	@Bean
//	@DependsOn("myBeanDefinitionRegistryPostProcessor")
//	public void init(){
//		System.out.println("init");
//	}
//}
