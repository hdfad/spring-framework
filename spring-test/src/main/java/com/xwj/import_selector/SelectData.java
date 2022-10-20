package com.xwj.import_selector;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//@Import({ServiceImportSelector.class,MyImportBeanDefinitionRegistrar.class,ImportOrdinaryClass.class,MyDeferredImportSelector.class})
@Import(MyDeferredImportSelector.class)
//@Import(ImportOrdinaryClass.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface SelectData {
	String dataSources() default "";
}
