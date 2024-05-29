package com.xwj.aware;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.xwj.aware")
public class AwareTest {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext(AwareTest.class);
		SpringRegister bean = annotationConfigApplicationContext.getBean(SpringRegister.class);

	}
}
