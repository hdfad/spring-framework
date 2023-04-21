package com.xwj.bean_factory;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2023/04/20 15:19
 */
@ComponentScan("com.xwj.bean_factory")
public class BeanFactoryTest {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext(BeanFactoryTest.class);
		ClassPathXmlApplicationContext classPathXmlApplicationContext=new ClassPathXmlApplicationContext("xwj/spring-bean.xml");
	}
}
