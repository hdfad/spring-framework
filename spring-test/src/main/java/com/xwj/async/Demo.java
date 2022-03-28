package com.xwj.async;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.xwj.async")
public class Demo {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext(Demo.class);
		AsyncService bean = annotationConfigApplicationContext.getBean(AsyncService.class);
		bean.doSomething();
		System.out.println("执行结束");
	}
}
