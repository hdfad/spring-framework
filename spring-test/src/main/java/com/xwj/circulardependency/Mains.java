package com.xwj.circulardependency;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/04/29 16:21
 */
@ComponentScan("com.xwj.circulardependency")
public class Mains {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext(Mains.class);
		A bean = annotationConfigApplicationContext.getBean(A.class);
		System.out.println(bean);
	}
}
