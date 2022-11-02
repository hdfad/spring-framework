package com.xwj.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/11/02 14:34
 */
@Aspect
@Component
public class AopProxy {

	@Before("execution(* com.xwj.aop.AService.test())")
	public void test(){
		System.out.println("=============AopProxy=============");
	}
}
