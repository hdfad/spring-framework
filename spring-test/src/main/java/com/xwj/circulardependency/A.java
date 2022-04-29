package com.xwj.circulardependency;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/04/29 16:20
 */
public class A {
	@Autowired
	private B b;
}
