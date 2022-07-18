package com.xwj.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/07/18 10:11
 */
@Component
public class MyPushClassListener implements ApplicationListener<MyPushEvent> {

	@Override
	public void onApplicationEvent(MyPushEvent event) {
		System.out.println("========================");
		System.out.println(event.getName());
		System.out.println("========================");
	}
}
