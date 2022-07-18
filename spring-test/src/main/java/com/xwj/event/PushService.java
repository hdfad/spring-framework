package com.xwj.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/07/18 10:04
 */
@Component
public class PushService {
	@Autowired
	public ApplicationContext applicationContext;

	public void doPush(String name){
		applicationContext.publishEvent(new MyPushEvent(this,name));
	}
}
