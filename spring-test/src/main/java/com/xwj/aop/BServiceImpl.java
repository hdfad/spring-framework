package com.xwj.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/11/03 11:24
 */
@Service
public class BServiceImpl implements BService{
	@Autowired
	AService aService;

	public void out(){
		aService.test();
	}
}
