package com.xwj.ioc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class A {
	@Autowired
	private B b;

	@Autowired(required = false)
	public A(B b) {
		this.b = b;
	}

	@Autowired(required = false)
	public A() {
	}
}
