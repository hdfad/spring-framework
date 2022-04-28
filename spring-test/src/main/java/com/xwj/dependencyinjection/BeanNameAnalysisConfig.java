package com.xwj.dependencyinjection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/04/28 15:18
 */
@Configuration
public class BeanNameAnalysisConfig {
	@Bean("aas")
	public A1 getA1(){
		return new A1();
	}
}


class A1{

}