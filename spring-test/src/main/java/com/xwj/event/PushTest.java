package com.xwj.event;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * 时间监听器
 * 1：事件监听器 ApplicationEvent
 * 2：事件监听器 ApplicationListener<T> OR @EventListener(class)
 * 3：上下文applicationContext#publishEvent
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/07/18 10:15
 */
@ComponentScan("com.xwj.event")
public class PushTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext(PushTest.class);
		PushService bean = annotationConfigApplicationContext.getBean(PushService.class);
		bean.doPush("xxxx");
	}
}
