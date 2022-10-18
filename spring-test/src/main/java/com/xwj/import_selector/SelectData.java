package com.xwj.import_selector;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Import({ServiceImportSelector.class,MyImportBeanDefinitionRegistrar.class})
@Retention(RetentionPolicy.RUNTIME)
public @interface SelectData {
	String dataSources() default "";
}
