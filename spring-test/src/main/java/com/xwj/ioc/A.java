package com.xwj.ioc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class A {
	@Autowired
	private B b;

	public void setB(B b) {
		this.b = b;
	}

	public B getB() {
		return b;
	}

	/*@Autowired
			private B b;
		*/
	public void getA(){
		System.out.println(1);
	}

	/*@Autowired(required = false)
	public A() {

	}*/

	/*String name;
	@Autowired(required = false)
	public A(String name) {
		this.name=name;
	}*/
}
