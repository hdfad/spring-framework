package com.xwj.ioc;

import org.springframework.context.weaving.LoadTimeWeaverAware;
import org.springframework.instrument.classloading.LoadTimeWeaver;


public class MyLoadTimeWeaverAware implements LoadTimeWeaverAware {

	public MyLoadTimeWeaverAware() {
		System.out.println("LoadTimeWeaverAwareImpl init");
	}

	@Override
	public void setLoadTimeWeaver(LoadTimeWeaver loadTimeWeaver) {
		System.out.println("调用了LoadTimeWeaverAware");
	}
}
