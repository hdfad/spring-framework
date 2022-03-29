package com.xwj.async;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.xwj.async")
public class Demo {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext(Demo.class);
		AsyncService bean = annotationConfigApplicationContext.getBean(AsyncService.class);
		int i=40;
		while (i>0){
			bean.doSomething();
			i--;
		}
		System.out.println(Thread.currentThread().getName()+"执行结束");
	}
}
