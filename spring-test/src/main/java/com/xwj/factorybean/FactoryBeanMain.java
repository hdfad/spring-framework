package com.xwj.factorybean;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/06/01 16:34
 */
@ComponentScan("com.xwj.factorybean")
public class FactoryBeanMain {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext(FactoryBeanMain.class);
		Object student = annotationConfigApplicationContext.getBean(Student.class);
		System.out.println(student);
	}
}
