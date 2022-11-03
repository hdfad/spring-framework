package com.xwj.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AServiceImpl implements AService {

	@Autowired
	BService bService;

	@Override
	public void test() {
		System.out.println("============AServiceTest============");
	}
}
