package com.xwj.aop;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@ComponentScan("com.xwj.aop")
@EnableAspectJAutoProxy
public class TestAop {
 
	public static void main(String[] args) {

		AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext(TestAop.class);
		Person bean2 = (Person)annotationConfigApplicationContext.getBean("student");
		bean2.say();
	}
}
