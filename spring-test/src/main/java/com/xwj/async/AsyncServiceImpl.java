package com.xwj.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncServiceImpl implements AsyncService{

	@Override
	@Async
	public void doSomething() {
		System.out.println(Thread.currentThread().getName()+"执行doSomething");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName()+"执行doSomething结束");
	}
}
