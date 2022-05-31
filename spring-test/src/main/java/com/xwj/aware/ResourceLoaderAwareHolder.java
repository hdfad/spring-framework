package com.xwj.aware;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/05/30 9:41
 */
@Component
public class ResourceLoaderAwareHolder implements ResourceLoaderAware {

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		System.out.println(resourceLoader);
	}
}
