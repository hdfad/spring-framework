package com.xwj.initialize_bean;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2023/04/12 10:48
 */
@ComponentScan("com.xwj.initialize_bean")
public class InitializeBeanTest {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext(InitializeBeanTest.class);
		Ia bean = annotationConfigApplicationContext.getBean(Ia.class);

	}
}

@Configuration
class Ia{

	@Bean(initMethod = "init",destroyMethod = "destory")
	public Ib ib(){
		return new Ib();
	}

	

}

@Component
class Ib{
	String c1;

	public void init(){
		System.out.println("init......");
	}

	public void destory(){
		System.out.println("调用销毁化方法....");
	}

	
	public String getC1() {
		return c1;
	}

	public void setC1(String c1) {
		this.c1 = c1;
	}
}

