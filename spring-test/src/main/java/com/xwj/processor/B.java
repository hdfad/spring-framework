package com.xwj.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/11/11 14:09
 */
@Component
public class B  {
	@Autowired
	AAAAA aaaaa;

	public void send(){
		System.out.println(aaaaa);
	}
}
