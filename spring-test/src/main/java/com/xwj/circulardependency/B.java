package com.xwj.circulardependency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/04/29 16:20
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class B {
	@Autowired
	private A a;

	public void setA(A a) {
		this.a = a;
	}

	public A getA() {
		return a;
	}
}
