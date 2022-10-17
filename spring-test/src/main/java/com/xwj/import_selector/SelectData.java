package com.xwj.import_selector;

import org.springframework.context.annotation.Import;

@Import(ServiceImportSelector.class)
public @interface SelectData {
	String dataSources() default "";
}
