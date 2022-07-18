package com.xwj.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/07/18 10:09
 */
public class MyPushEvent extends ApplicationEvent implements java.io.Serializable{
	private static final long serialVersionUID = 0L;

	private String name;

	public MyPushEvent(Object source,String name) {
		super(source);
		this.name=name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
