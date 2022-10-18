package com.xwj.import_selector;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 *
 * 对于扩展：
 * ImportBeanDefinitionRegistrar和ImportSelector都可以对Import的类进行操作，
 * 但是ImportSelector只能返回bean，而ImportBeanDefinitionRegistrar可以操作bean的BeanDefinition
 *
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/10/18 11:24
 */
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		/*//构建一个 BeanDefinition , Bean的类型为 UserConfig,这个Bean的属性username的值为后端元宇宙
		AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(UserConfig.class)
				.addPropertyValue("username", "后端元宇宙")
				.getBeanDefinition();
		//把 UserConfig 这个Bean的定义注册到容器中
		registry.registerBeanDefinition("userConfig", beanDefinition);*/

		BeanDefinition beanDefinition = registry.getBeanDefinition(AService.class.getName());

	}
}
