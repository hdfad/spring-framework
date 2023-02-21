package com.xwj.ioc;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2023/02/21 12:07
 */
@Component
public class F {

	@Bean(autowire=Autowire.BY_TYPE)
	public C c(){
		return new C();
	}

}
