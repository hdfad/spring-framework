package com.xwj.lookupmethod;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestLookupMethod {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:META-INF/spring-bean-look-up.xml");
		CommandManager manager = context.getBean("manager", CommandManager.class);
		System.out.println(manager.getClass().getName());
		manager.process();
	}
}

abstract class CommandManager {
	public void process() {
		MyCommand command = createCommand(); //dosomething... System.out.println(command);
	}

	protected abstract MyCommand createCommand();
}

class MyCommand {
	public MyCommand() {
		System.out.println("MyCommandinstanced");
	}
}