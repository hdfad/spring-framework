package com.xwj.import_selector;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/10/20 10:59
 */
public class MyDeferredImportSelector implements DeferredImportSelector {
	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		return new String[0];
	}
}
