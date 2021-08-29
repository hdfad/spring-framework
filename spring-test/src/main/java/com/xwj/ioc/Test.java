package com.xwj.ioc;

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.*;

@ComponentScan("com.xwj.ioc")
@Configuration
@Import(value = { MyImportBeanDefinitionRegistrar.class})
public class Test {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext(Test.class);
		A c = annotationConfigApplicationContext.getBean(A.class);
		System.out.println(c);
	}
}
