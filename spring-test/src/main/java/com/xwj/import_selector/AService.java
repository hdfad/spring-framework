package com.xwj.import_selector;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/10/17 18:03
 */
public class AService extends Services{

	String desc="a";

	@Override
	void setData(String data) {
		System.out.println(desc+data);
	}
}
