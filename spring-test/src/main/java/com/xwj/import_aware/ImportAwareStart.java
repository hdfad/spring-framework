package com.xwj.import_aware;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/10/14 15:22
 */
@ComponentScan("com.xwj.import_aware")
public class ImportAwareStart {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext(ImportAwareStart.class);
	}
}
