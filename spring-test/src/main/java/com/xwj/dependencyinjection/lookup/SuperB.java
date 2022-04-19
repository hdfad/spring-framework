package com.xwj.dependencyinjection.lookup;

import org.springframework.beans.factory.annotation.Lookup;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/04/19 15:25
 */
public class SuperB {
	public AA aa;
	@Lookup("aaa")
	public AA getAa() {
		return aa;
	}
}
