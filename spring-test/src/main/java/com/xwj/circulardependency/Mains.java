package com.xwj.circulardependency;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/04/29 16:21
 */
public class Mains {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.register(A.class,B.class);
		annotationConfigApplicationContext.refresh();
		A bean = annotationConfigApplicationContext.getBean(A.class);
		System.out.println(bean);
	}
}
