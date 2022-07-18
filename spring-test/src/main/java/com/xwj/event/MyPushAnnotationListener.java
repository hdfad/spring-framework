package com.xwj.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/07/18 10:42
 */
@Component
public class MyPushAnnotationListener {
	@EventListener(MyPushEvent.class)
	public void doListener(MyPushEvent event){
		System.out.println("===========22222=============");
		System.out.println(event.getName());
		System.out.println("===========22222=============");

	}
}
