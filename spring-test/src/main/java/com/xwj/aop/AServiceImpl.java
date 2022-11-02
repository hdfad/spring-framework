package com.xwj.aop;

import org.springframework.stereotype.Service;


@Service
public class AServiceImpl implements AService {


	@Override
	public void test() {
		System.out.println("============AServiceTest============");
	}
}
