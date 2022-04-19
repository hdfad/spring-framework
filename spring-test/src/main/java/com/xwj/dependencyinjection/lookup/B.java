package com.xwj.dependencyinjection.lookup;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Component
public class B extends SuperB{
	public AA aa;

	@Lookup
	public AA getAa() {
		return aa;
	}
}