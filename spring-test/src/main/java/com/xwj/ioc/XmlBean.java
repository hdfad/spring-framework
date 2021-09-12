package com.xwj.ioc;

public class XmlBean {
	String id;

	public void setId(String id) {
		this.id = id;
	}

	public XmlBean(String id) {
		this.id = id;
		System.out.println("XmlBean(String id)");
	}

	public XmlBean() {
		System.out.println("XmlBean");
	}
}
