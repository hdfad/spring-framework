package com.xwj.ioc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {

	@Bean(name = {"e","e1","e2","e3"})
	public E getE(){
		return new E();
	}
}
