package com.xwj.spi;

import com.xwj.spi.service.SpringSpiInterface;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

@ComponentScan("com.xwj.spi")
@Configuration
public class Test {
	public static void main(String[] args) {
		List<SpringSpiInterface> tests = SpringFactoriesLoader.loadFactories(SpringSpiInterface.class, SpringSpiInterface.class.getClassLoader());
		for(SpringSpiInterface springSpiInterface:tests){
			springSpiInterface.sayHello();
		}
	}
}