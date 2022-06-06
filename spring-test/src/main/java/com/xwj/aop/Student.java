package com.xwj.aop;

import org.springframework.stereotype.Component;

@Component
public class Student implements Person{

	@Override
	public void say(){
		System.out.println("=================say================");
	}

	@Override
	public void fight() {
		System.out.println("=================fight================");
	}
}