package com.xwj.aop;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/11/02 14:43
 */
@ComponentScan("com.xwj.aop")
@EnableAspectJAutoProxy
public class StartService {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext(StartService.class);
		AService bean = annotationConfigApplicationContext.getBean(AService.class);
		bean.test();
		System.out.println(bean);
	}
}
