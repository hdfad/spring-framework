/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.context.annotation;

import java.util.Arrays;
import java.util.function.Supplier;

import org.springframework.beans.factory.config.BeanDefinitionCustomizer;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.metrics.StartupStep;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Standalone application context, accepting <em>component classes</em> as input &mdash;
 * in particular {@link Configuration @Configuration}-annotated classes, but also plain
 * {@link org.springframework.stereotype.Component @Component} types and JSR-330 compliant
 * classes using {@code javax.inject} annotations.
 *
 * <p>Allows for registering classes one by one using {@link #register(Class...)}
 * as well as for classpath scanning using {@link #scan(String...)}.
 *
 * <p>In case of multiple {@code @Configuration} classes, {@link Bean @Bean} methods
 * defined in later classes will override those defined in earlier classes. This can
 * be leveraged to deliberately override certain bean definitions via an extra
 * {@code @Configuration} class.
 *
 * <p>See {@link Configuration @Configuration}'s javadoc for usage examples.
 *
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 3.0
 * @see #register
 * @see #scan
 * @see AnnotatedBeanDefinitionReader
 * @see ClassPathBeanDefinitionScanner
 * @see org.springframework.context.support.GenericXmlApplicationContext
 *
 * extends GenericApplicationContext:本就是一个BeanDefinitionRegistry
 */
public class AnnotationConfigApplicationContext extends GenericApplicationContext implements AnnotationConfigRegistry {

	private final AnnotatedBeanDefinitionReader reader;

	private final ClassPathBeanDefinitionScanner scanner;


	/**
	 * Create a new AnnotationConfigApplicationContext that needs to be populated
	 * through {@link #register} calls and then manually {@linkplain #refresh refreshed}.
	 *
	 *	父类构造方法：
	 *	@see DefaultResourceLoader：啥都没干，提供了对资源加载器的支持
	 *  @see AbstractApplicationContext：配置部分bean名称，与国际化、生命周期bean名称、多播器，以及spel配置表达式是否支持，这部分参数在refresh时会使用到
	 *  @see GenericApplicationContext：继承AbstractApplicationContext，配置默认bean工厂，
	 *  	在bean工厂中会通过父类实现对AbstractAutowireCapableBeanFactory来支持bean别名处理，单例创建支持，factoryBean支持
	 *  	对部分aware的忽略设置和bean实例创建策略
	 *  父接口：
	 *  @see ConfigurableApplicationContext：提供系统运行环境参数 {@link ConfigurableApplicationContext#getEnvironment()}
	 *  @see BeanDefinitionRegistry：对BeanDefinition注册和移除提供支持
	 *
	 *
	 *
	 */
	public AnnotationConfigApplicationContext() {
		StartupStep createAnnotatedBeanDefReader = this.getApplicationStartup().start("spring.context.annotated-bean-reader.create");
		/*
		*	初始化BeanDefinitionReader，实例化DefaultListableBeanFactory工厂 完成Spring部分注解的支持
		*	AnnotatedBeanDefinitionReader reader=new AnnotatedBeanDefinitionReader(BeanDefinitionRegistry,Environment);
		*	提供对
		* 	@Component、@Repository、@Service、@Controller、@ManagedBean、@Named   -> @see ClassPathScanningCandidateComponentProvider#registerDefaultFilters
		* 	@Order、@Priority、@Qualifier、@Value、@Configuration、@Autowired、
		* 	@Required、@Resource、@PostConstruct、@PreDestroy、@PersistenceContext、@EventListener	-> @see AnnotationConfigUtils#registerAnnotationConfigProcessors
		* 	this->BeanDefinitionRegistry：包含唯一id和展示名称，2者相同，使用AbstractApplicationContext#id、displayName，2者为为className+调用对象的hash取十六进制字符串
		 */
		/**
		 * AnnotationConfigApplicationContext 是 BeanDefinitionRegistry子类，所以当前this指代的是BeanDefinitionRegistry
		 */
		this.reader = new AnnotatedBeanDefinitionReader(this);
		createAnnotatedBeanDefReader.end();
		//初始化一个类扫描器，扫描 @Component、@Repository、@Service、@Controller、@ManagedBean、@Named
		this.scanner = new ClassPathBeanDefinitionScanner(this);
	}

	/**
	 * Create a new AnnotationConfigApplicationContext with the given DefaultListableBeanFactory.
	 * @param beanFactory the DefaultListableBeanFactory instance to use for this context
	 */
	public AnnotationConfigApplicationContext(DefaultListableBeanFactory beanFactory) {
		super(beanFactory);
		this.reader = new AnnotatedBeanDefinitionReader(this);
		this.scanner = new ClassPathBeanDefinitionScanner(this);
	}

	/**
	 * Create a new AnnotationConfigApplicationContext, deriving bean definitions
	 * from the given component classes and automatically refreshing the context.
	 * @param componentClasses one or more component classes &mdash; for example,
	 * {@link Configuration @Configuration} classes
	 *
	 *
	 * 1:调用无参构造方法，优先加载父类的静态属性和构造方法
	 *  有三个父类：
	 *		DefaultResourceLoader：提供资源加载器的支持
	 *	    AbstractApplicationContext：提供部分内置默认bean名称：
	 *	    								国际化MessageSourceBeanName，
	 *	        							生命周期lifecycleProcessorBeanName,
	 *	        							多播器applicationEventMulticasterName
	 *	        						除此之外，还判断是否启用了spel的支持
	 *	        						这部分参数在refresh时会被使用到
	 *	     GenericApplicationContext：实例化bean工厂-DefaultListableBeanFactory，
	 *	                       		    然后通过bean工厂的父类AbstractAutowireCapableBeanFactory提供对bean别名、单例对象、factoryBean的支持，
	 *	                       			然后设置对部分Aware接口的忽略，判断对象生成策略，
	 *	                       				有2种：SimpleInstantiationStrategy和CglibSubclassingInstantiationStrategy，根据GraalVM判断
	 *
	 *
	 */
	public AnnotationConfigApplicationContext(Class<?>... componentClasses) {
		/**
		 * 调用当前无参构造方法，从父类开始的静态方法和静态代码块开始
		 */
		this();
		//使用reder构建BeanDefinition，根据类class，初始化容器，注册bean，构建BeanDefinition

		/*
		*
		* 调用doRegister，根据beanClass构建AnnotatedGenericBeanDefinition，接下来会对AnnotatedGenericBeanDefinition进行赋值
		* 首先会生成BeanName，使用BeanNameGenerator#generateBeanName；
		* 再设置BeanDefinition的属性值，判断是否存在Lazy、Primary、DependsOn、Role、Description注解描述的beanDefinition
		* 未完：2021-09-13
		* */
		register(componentClasses);
		//初始化上下文，进行bean信息装配
		refresh();
	}

	/**
	 * Create a new AnnotationConfigApplicationContext, scanning for components
	 * in the given packages, registering bean definitions for those components,
	 * and automatically refreshing the context.
	 * @param basePackages the packages to scan for component classes
	 */
	public AnnotationConfigApplicationContext(String... basePackages) {
		this();
		scan(basePackages);
		refresh();
	}


	/**
	 * Propagate the given custom {@code Environment} to the underlying
	 * {@link AnnotatedBeanDefinitionReader} and {@link ClassPathBeanDefinitionScanner}.
	 */
	@Override
	public void setEnvironment(ConfigurableEnvironment environment) {
		super.setEnvironment(environment);
		this.reader.setEnvironment(environment);
		this.scanner.setEnvironment(environment);
	}

	/**
	 * Provide a custom {@link BeanNameGenerator} for use with {@link AnnotatedBeanDefinitionReader}
	 * and/or {@link ClassPathBeanDefinitionScanner}, if any.
	 * <p>Default is {@link AnnotationBeanNameGenerator}.
	 * <p>Any call to this method must occur prior to calls to {@link #register(Class...)}
	 * and/or {@link #scan(String...)}.
	 * @see AnnotatedBeanDefinitionReader#setBeanNameGenerator
	 * @see ClassPathBeanDefinitionScanner#setBeanNameGenerator
	 * @see AnnotationBeanNameGenerator
	 * @see FullyQualifiedAnnotationBeanNameGenerator
	 */
	public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
		this.reader.setBeanNameGenerator(beanNameGenerator);
		this.scanner.setBeanNameGenerator(beanNameGenerator);
		getBeanFactory().registerSingleton(
				AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR, beanNameGenerator);
	}

	/**
	 * Set the {@link ScopeMetadataResolver} to use for registered component classes.
	 * <p>The default is an {@link AnnotationScopeMetadataResolver}.
	 * <p>Any call to this method must occur prior to calls to {@link #register(Class...)}
	 * and/or {@link #scan(String...)}.
	 */
	public void setScopeMetadataResolver(ScopeMetadataResolver scopeMetadataResolver) {
		this.reader.setScopeMetadataResolver(scopeMetadataResolver);
		this.scanner.setScopeMetadataResolver(scopeMetadataResolver);
	}


	//---------------------------------------------------------------------
	// Implementation of AnnotationConfigRegistry
	//---------------------------------------------------------------------

	/**
	 * Register one or more component classes to be processed.
	 * <p>Note that {@link #refresh()} must be called in order for the context
	 * to fully process the new classes.
	 * @param componentClasses one or more component classes &mdash; for example,
	 * {@link Configuration @Configuration} classes
	 * @see #scan(String...)
	 * @see #refresh()
	 *
	 *
	 * 使用reder构建BeanDefinition添加到beanDefinitionMap中，对存在别名的添加别名映射,k:别名，v:真实名称
	 */
	@Override
	public void register(Class<?>... componentClasses) {
		Assert.notEmpty(componentClasses, "At least one component class must be specified");
		StartupStep registerComponentClass = this.getApplicationStartup().start("spring.context.component-classes.register")
				.tag("classes", () -> Arrays.toString(componentClasses));
		/*
		* 对已经初始化完毕的AnnotatedBeanDefinitionReader开始注册bean
		* reader在上一步初始化构建。
		* 设置AnnotatedGenericBeanDefinition的作用域，Lazy、Primary、DependsOn、Role、Description注解的支持、别名，
		* 使用BeanDefinitionHolder将beanDefinition添加到beanDefinitionMap中，对存在别名的添加别名映射,k:别名，v:真实名称
		*  */
		this.reader.register(componentClasses);
		registerComponentClass.end();
	}

	/**
	 * Perform a scan within the specified base packages.
	 * <p>Note that {@link #refresh()} must be called in order for the context
	 * to fully process the new classes.
	 * @param basePackages the packages to scan for component classesenumConstants = null
	 * @see #register(Class...)
	 * @see #refresh()
	 */
	@Override
	public void scan(String... basePackages) {
		Assert.notEmpty(basePackages, "At least one base package must be specified");
		StartupStep scanPackages = this.getApplicationStartup().start("spring.context.base-packages.scan")
				.tag("packages", () -> Arrays.toString(basePackages));
		this.scanner.scan(basePackages);
		scanPackages.end();
	}


	//---------------------------------------------------------------------
	// Adapt superclass registerBean calls to AnnotatedBeanDefinitionReader
	//---------------------------------------------------------------------

	@Override
	public <T> void registerBean(@Nullable String beanName, Class<T> beanClass,
			@Nullable Supplier<T> supplier, BeanDefinitionCustomizer... customizers) {

		this.reader.registerBean(beanClass, beanName, supplier, customizers);
	}

}
