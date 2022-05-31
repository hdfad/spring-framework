package com.xwj.aware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/05/30 9:46
 */
@ComponentScan("com.xwj.aware")
public class AwareTest {



	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext(AwareTest.class);
		System.out.println(annotationConfigApplicationContext.getBeanDefinitionCount());
		Utils.setA();
		Utils.getA();
	}
}
