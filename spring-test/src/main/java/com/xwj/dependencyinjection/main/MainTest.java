package com.xwj.dependencyinjection.main;

import com.xwj.dependencyinjection.TestService;
import com.xwj.dependencyinjection.impl.TestServiceImpl;
import com.xwj.dependencyinjection.lookup.AA;
import com.xwj.dependencyinjection.lookup.B;
import com.xwj.dependencyinjection.lookup.LookUpTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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

	String parameter;

/*	private MainTest(){
		System.out.println("无参");
	}

	private MainTest(String parameter,TestService testService){
		this.parameter=parameter;
		this.testServiceImpl2=testService;
		System.out.println("2个参数");
	}*/

	public MainTest(TestServiceImpl testService){
		this.testServiceImpl2=testService;
		System.out.println("有参");
	}

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.register(MainTest.class, TestServiceImpl.class);
//		context.register(LookUpTest.class, AA.class, B.class);
		context.refresh();
//		System.out.println(context.getBean(MainTest.class).bean_111.hashCode());
//		System.out.println(context.getBean(MainTest.class).testServiceImpl2.hashCode());
//		LookUpTest bean = context.getBean(LookUpTest.class);
//		bean.aa();

	}
}
