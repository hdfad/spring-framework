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
