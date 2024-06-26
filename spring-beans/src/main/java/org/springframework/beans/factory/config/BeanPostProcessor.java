/*
 * Copyright 2002-2019 the original author or authors.
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

package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;
import org.springframework.lang.Nullable;

/**
 * Factory hook that allows for custom modification of new bean instances &mdash;
 * for example, checking for marker interfaces or wrapping beans with proxies.
 *
 * <p>Typically, post-processors that populate beans via marker interfaces
 * or the like will implement {@link #postProcessBeforeInitialization},
 * while post-processors that wrap beans with proxies will normally
 * implement {@link #postProcessAfterInitialization}.
 *
 * <h3>Registration</h3>
 * <p>An {@code ApplicationContext} can autodetect {@code BeanPostProcessor} beans
 * in its bean definitions and apply those post-processors to any beans subsequently
 * created. A plain {@code BeanFactory} allows for programmatic registration of
 * post-processors, applying them to all beans created through the bean factory.
 *
 * <h3>Ordering</h3>
 * <p>{@code BeanPostProcessor} beans that are autodetected in an
 * {@code ApplicationContext} will be ordered according to
 * {@link org.springframework.core.PriorityOrdered} and
 * {@link org.springframework.core.Ordered} semantics. In contrast,
 * {@code BeanPostProcessor} beans that are registered programmatically with a
 * {@code BeanFactory} will be applied in the order of registration; any ordering
 * semantics expressed through implementing the
 * {@code PriorityOrdered} or {@code Ordered} interface will be ignored for
 * programmatically registered post-processors. Furthermore, the
 * {@link org.springframework.core.annotation.Order @Order} annotation is not
 * taken into account for {@code BeanPostProcessor} beans.
 *
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 10.10.2003
 * @see InstantiationAwareBeanPostProcessor
 * @see DestructionAwareBeanPostProcessor
 * @see ConfigurableBeanFactory#addBeanPostProcessor
 * @see BeanFactoryPostProcessor
 *
 * <p>
 * 		负责对象在初始化前后的处理流程
 * 		(InstantiationAwareBeanPostProcessor负责对象在实例化过程中的处理流程)
 * </p>
 *
 * bean后置处理器顶层接口，spring生命周期中的后置处理器扩展点都是BeanPostProcessor的子类，用于在bean的**初始化前**和**初始化后**的各种扩展
 * 在BeanPostProcessor接口中有2个Nullable注解标注得Initialization方法：
 * 		postProcessBeforeInitialization：bean初始化之前执行
 * 		init-method
 * 		postProcessAfterInitialization：bean初始化之后执行
 * 	有默认返回值：传入的bean对象
 *
 *
 * 	bean创建过程的后置处理器调用流程：
 * 		   InstantiationAwareBeanPostProcessor # postProcessBeforeInstantiation
 * 		-> BeanPostProcessor # postProcessAfterInitialization
 * 		-> SmartInstantiationAwareBeanPostProcessor # determineCandidateConstructors
 * 		-> MergedBeanDefinitionPostProcessor # postProcessMergedBeanDefinition
 * 	 	-> InstantiationAwareBeanPostProcessor # postProcessAfterInstantiation
 * 	 	-> InstantiationAwareBeanPostProcessor # postProcessProperties
 * 	 	-> InstantiationAwareBeanPostProcessor # postProcessPropertyValues
 * 	 	-> BeanPostProcessor # postProcessBeforeInitialization
 * 	 	-> BeanPostProcessor # postProcessAfterInitialization
 *
 * 	 调用的顶层后置处理器接口就4种 InstantiationAwareBeanPostProcessor -> BeanPostProcessor -> SmartInstantiationAwareBeanPostProcessor ->MergedBeanDefinitionPostProcessor
 *
 */
public interface BeanPostProcessor {

	/**
	 * Apply this {@code BeanPostProcessor} to the given new bean instance <i>before</i> any bean
	 * initialization callbacks (like InitializingBean's {@code afterPropertiesSet}
	 * or a custom init-method). The bean will already be populated with property values.
	 * The returned bean instance may be a wrapper around the original.
	 * <p>The default implementation returns the given {@code bean} as-is.
	 * @param bean the new bean instance
	 * @param beanName the name of the bean
	 * @return the bean instance to use, either the original or a wrapped one;
	 * if {@code null}, no subsequent BeanPostProcessors will be invoked
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet
	 *
	 * 在初始化前对bean进行操作,在bean生命周期中只会被调用一次
	 * 调用此接口前bean已经被实例化完成，并且完成了属性的填充，因此这个过程属于后续的bean的初始化过程，此接口在初始化之前调用
	 *
	 * 调用链
	 * AbstractAutowireCapableBeanFactory # createBean
	 * |____AbstractAutowireCapableBeanFactory # doCreateBean
	 * 		|____AbstractAutowireCapableBeanFactory # initializeBean
	 * 			 |____AbstractAutowireCapableBeanFactory # applyBeanPostProcessorsBeforeInitialization
	 *
	 */
	@Nullable
	default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	/**
	 * Apply this {@code BeanPostProcessor} to the given new bean instance <i>after</i> any bean
	 * initialization callbacks (like InitializingBean's {@code afterPropertiesSet}
	 * or a custom init-method). The bean will already be populated with property values.
	 * The returned bean instance may be a wrapper around the original.
	 * <p>In case of a FactoryBean, this callback will be invoked for both the FactoryBean
	 * instance and the objects created by the FactoryBean (as of Spring 2.0). The
	 * post-processor can decide whether to apply to either the FactoryBean or created
	 * objects or both through corresponding {@code bean instanceof FactoryBean} checks.
	 * <p>This callback will also be invoked after a short-circuiting triggered by a
	 * {@link InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation} method,
	 * in contrast to all other {@code BeanPostProcessor} callbacks.
	 * <p>The default implementation returns the given {@code bean} as-is.
	 * @param bean the new bean instance
	 * @param beanName the name of the bean
	 * @return the bean instance to use, either the original or a wrapped one;
	 * if {@code null}, no subsequent BeanPostProcessors will be invoked
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet
	 * @see org.springframework.beans.factory.FactoryBean
	 *
	 * 在初始化后对bean进行操作
	 *
	 * 在bean生命周期中会调用2次，但是调用阶段都是在bean初始化之后，对bean进行一些操作p
	 * 第一次调用是发生在如果 InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation返回非空时对初始化完成的bean进行后续操作
	 * 调用链
	 *  AbstractAutowireCapableBeanFactory # createBean
	 * 	|____AbstractAutowireCapableBeanFactory # resolveBeforeInstantiation
	 * 		 |____AbstractAutowireCapableBeanFactory # applyBeanPostProcessorsAfterInitialization
	 *
	 * 	第二次调用发生在 AbstractAutowireCapableBeanFactory # initializeBean，
	 * 	进入此步或者第二次后置处理器调用都需要第一次InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation返回null
	 * 	才会经历后续步骤调用，也才会经过doCreateBean创建初始化bean，在initializeBean阶段通过applyBeanPostProcessorsAfterInitialization对postProcessAfterInitialization进行调用
	 * 	调用链
	 * AbstractAutowireCapableBeanFactory # createBean
	 * |____AbstractAutowireCapableBeanFactory # doCreateBean
	 * 		|____AbstractAutowireCapableBeanFactory # initializeBean
	 * 			 |____AbstractAutowireCapableBeanFactory # applyBeanPostProcessorsAfterInitialization
	 *
	 *
	 */
	@Nullable
	default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
