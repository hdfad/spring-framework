package com.xwj.import_selector;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/10/17 18:09
 */
@ComponentScan("com.xwj.import_selector")
@SelectData(dataSources = "a")
public class MyImportSelectMain {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext(MyImportSelectMain.class);
		Services bean = annotationConfigApplicationContext.getBean(Services.class);
		bean.setData("---------------");
	}
}
