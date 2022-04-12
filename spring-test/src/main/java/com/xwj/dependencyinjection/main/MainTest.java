package com.xwj.dependencyinjection.main;

import com.xwj.dependencyinjection.TestService;
import com.xwj.dependencyinjection.impl.TestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.Resource;
import javax.inject.Inject;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/04/12 11:12
 */
public class MainTest {
	// 随便指定beanName
/*	@Resource
	private TestService bean_111;*/

/*
	@Autowired
	private TestService bean6664;
*/

	@Resource
	private TestService TestServiceImpl2;

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.register(MainTest.class, TestServiceImpl.class);
		context.refresh();
//		System.out.println(context.getBean(MainTest.class).bean_111.hashCode());
		System.out.println(context.getBean(MainTest.class).TestServiceImpl2.hashCode());
	}
}
