package com.xwj.import_selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/10/17 18:05
 */
public class ServiceImportSelector implements ImportSelector {
	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(SelectData.class.getName());
		if("A".equals(String.valueOf(annotationAttributes.get("dataSources")))){
			return new String[]{
					AService.class.getName()
			};
		}else if ("B".equals(String.valueOf(annotationAttributes.get("dataSources")))){
			return new String[]{
					BService.class.getName()
			};
		}else {
			return new String[0];
		}
	}
}
