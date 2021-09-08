package com.xwj.ioc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfigs {

	@Bean(value = {"cs","cs2","cs3"})
	public C c(){
		return new C();
	}
	@Bean("ds")
	public D d(){
		return new D();
	}
}
