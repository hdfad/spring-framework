package com.xwj.processor;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/11/11 14:09
 */
@ComponentScan("com.xwj.processor")
public class Start {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext(Start.class);
		
	}
}
