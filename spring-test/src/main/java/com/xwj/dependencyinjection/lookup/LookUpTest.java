package com.xwj.dependencyinjection.lookup;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/04/19 14:41
 */
public class LookUpTest {

	@Autowired
	B b;

	public void aa(){
		System.out.println(b.getAa());
		System.out.println(b.getAa());
		System.out.println(b.getAa());
		System.out.println(b.getAa());
	}

}

