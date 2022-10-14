package com.xwj.import_aware;

import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/10/14 12:10
 */
@Component
public class MyImportAware implements ImportAware {
	private String importTest;
	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		System.out.println("MyImportAware");

		//这里importMetadata不是ImportTest而是@ImportTest注解标注的类,这里就是Car
		//经常通过这种方式获取注解的信息
		Map<String, Object> map = importMetadata.getAnnotationAttributes (ImportTest.class.getName ());
		AnnotationAttributes attrs = AnnotationAttributes.fromMap(map);
		importTest = attrs.getString ("importTest");
	}
}
