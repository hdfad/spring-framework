package com.xwj.spi.service.impl;

import com.xwj.spi.service.SpringSpiInterface;

public class SpringSpiInterfaceAImpl implements SpringSpiInterface {
	@Override
	public void sayHello() {
		System.out.println("SpringSpiInterfaceAImpl hello");
	}
}
