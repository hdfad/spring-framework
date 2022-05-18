package com.xwj.resolveBeforeInstantiation_parse;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/05/18 14:16
 */
public class TestMain {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		applicationContext.register(MyConfig.class);
		applicationContext.refresh();
		MyResolveBeforeInstantiationConfig myBeforeInstantiation = applicationContext.getBean(MyResolveBeforeInstantiationConfig.class);
		System.out.println(myBeforeInstantiation.age);
	}
}
