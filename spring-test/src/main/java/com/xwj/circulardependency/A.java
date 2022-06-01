package com.xwj.circulardependency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/04/29 16:20
 */
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
}
