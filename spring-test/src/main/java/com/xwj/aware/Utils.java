package com.xwj.aware;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/05/30 15:30
 */
public final class Utils {
	public static final A a=(A)ApplicationContextHolder.getBean("a");

	public static void setA(){
		a.setName("121");
	}

	public static void getA(){
		System.out.println(a.getName());
	}
}
