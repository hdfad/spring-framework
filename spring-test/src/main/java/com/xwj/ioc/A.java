package com.xwj.ioc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class A {
	@Autowired
	private B b;

//	@Resource
//	private B b;


//	@Autowired
	public C c;

	@Autowired(required = false)
	public A(B b) {
		this.b = b;
	}

	@Autowired(required = false)
	public A() {
	}

	public void InstantiationC(){
		c=new C();
	}

	@Autowired
	public void init(){
		System.out.println("================================");
	}
}
