package com.xwj.dependencyinjection.main;

import com.xwj.dependencyinjection.TestService;
import com.xwj.dependencyinjection.impl.TestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

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

	@Autowired
	private TestService testServiceImpl2;

/*	public MainTest(){
		System.out.println("无参");
	}*/

	public MainTest(TestService testService){
		this.testServiceImpl2=testService;
		System.out.println("有参");
	}

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.register(MainTest.class, TestServiceImpl.class);
		context.refresh();
//		System.out.println(context.getBean(MainTest.class).bean_111.hashCode());
		System.out.println(context.getBean(MainTest.class).testServiceImpl2.hashCode());
	}
}
