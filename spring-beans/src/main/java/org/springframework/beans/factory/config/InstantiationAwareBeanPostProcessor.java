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

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.lang.Nullable;

/**
 * Subinterface of {@link BeanPostProcessor} that adds a before-instantiation callback,
 * and a callback after instantiation but before explicit properties are set or
 * autowiring occurs.
 *
 * <p>Typically used to suppress default instantiation for specific target beans,
 * for example to create proxies with special TargetSources (pooling targets,
 * lazily initializing targets, etc), or to implement additional injection strategies
 * such as field injection.
 *
 * <p><b>NOTE:</b> This interface is a special purpose interface, mainly for
 * internal use within the framework. It is recommended to implement the plain
 * {@link BeanPostProcessor} interface as far as possible, or to derive from
 * {@link InstantiationAwareBeanPostProcessorAdapter} in order to be shielded
 * from extensions to this interface.
 *
 *
 * @author Juergen Hoeller
 * @author Rod Johnson
 * @since 1.2
 * @see org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#setCustomTargetSourceCreators
 * @see org.springframework.aop.framework.autoproxy.target.LazyInitTargetSourceCreator
 *
 *
 * InstantiationAwareBean 后处理器
 *
 * 继承自BeanPostProcessor接口，在实例化前后对bean进行操作
 *
 *
 * 实例化 Instantiation：即将生成对象，对象还未生成
 * 初始化 Initialization：已经生成对象，对对象进行赋值
 *
 * bean实例化过程中会调用此后置处理器，首先会调用的后置处理器
 * 过程中会在postProcessProperties阶段调用部分注解
 *
 *
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

	/**
	 * Apply this BeanPostProcessor <i>before the target bean gets instantiated</i>.
	 * The returned bean object may be a proxy to use instead of the target bean,
	 * effectively suppressing default instantiation of the target bean.
	 * <p>If a non-null object is returned by this method, the bean creation process
	 * will be short-circuited. The only further processing applied is the
	 * {@link #postProcessAfterInitialization} callback from the configured
	 * {@link BeanPostProcessor BeanPostProcessors}.
	 * <p>This callback will be applied to bean definitions with their bean class,
	 * as well as to factory-method definitions in which case the returned bean type
	 * will be passed in here.
	 * <p>Post-processors may implement the extended
	 * {@link SmartInstantiationAwareBeanPostProcessor} interface in order
	 * to predict the type of the bean object that they are going to return here.
	 * <p>The default implementation returns {@code null}.
	 * @param beanClass the class of the bean to be instantiated
	 * @param beanName the name of the bean
	 * @return the bean object to expose instead of a default instance of the target bean,
	 * or {@code null} to proceed with default instantiation
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see #postProcessAfterInstantiation
	 * @see org.springframework.beans.factory.support.AbstractBeanDefinition#getBeanClass()
	 * @see org.springframework.beans.factory.support.AbstractBeanDefinition#getFactoryMethodName()
	 *
	 *
	 *
	 * 整个bean创建周期中只会调用一次，在createBean阶段，此时的bean处于未实例化
	 * 默认返回null，在实例化前不会对bean进行操作
	 * 如果返回非null对象，bean的创建过程就会短路，实例化阶段就完成了，不再进行doCreateBean流程
	 * 如果创建了代理类在此步就会直接返回
	 * 调用链：
	 *  AbstractAutowireCapableBeanFactory # createBean
	 * 	|____AbstractAutowireCapableBeanFactory # resolveBeforeInstantiation
	 * 		|____AbstractAutowireCapableBeanFactory # applyBeanPostProcessorsBeforeInstantiation
	 *
	 *
	 */
	@Nullable
	default Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		return null;
	}

	/**
	 * Perform operations after the bean has been instantiated, via a constructor or factory method,
	 * but before Spring property population (from explicit properties or autowiring) occurs.
	 * <p>This is the ideal callback for performing custom field injection on the given bean
	 * instance, right before Spring's autowiring kicks in.
	 * <p>The default implementation returns {@code true}.
	 * @param bean the bean instance created, with properties not having been set yet
	 * @param beanName the name of the bean
	 * @return {@code true} if properties should be set on the bean; {@code false}
	 * if property population should be skipped. Normal implementations should return {@code true}.
	 * Returning {@code false} will also prevent any subsequent InstantiationAwareBeanPostProcessor
	 * instances being invoked on this bean instance.
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see #postProcessBeforeInstantiation
	 *
	 * 实例化bean后对bean进行属性填充进行判断，处理实例化bean信息
	 *
	 * 整个bean生命周期中会被调用一次，在实例化后调用，发生在populateBean阶段，此时的bean已经被实例化完成，
	 * 不同于父接口BeanPostProcessor的postProcessAfterInitialization在初始化后对bean进行操作，
	 * postProcessBeforeInstantiation在实例化bean对象后对bean进行操作
	 * 调用链
	 * AbstractAutowireCapableBeanFactory # createBean
	 * |____AbstractAutowireCapableBeanFactory # doCreateBean
	 * 		|____AbstractAutowireCapableBeanFactory # populateBean
	 *
	 *
	 * 	返回值默认是true，自定义对bean的实例化后操作
	 * 	当返回false后不会对bean进行属性填充populateBean,直接结束populateBean
	 *
	 */
	default boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
		return true;
	}

	/**
	 * Post-process the given property values before the factory applies them
	 * to the given bean, without any need for property descriptors.
	 * <p>Implementations should return {@code null} (the default) if they provide a custom
	 * {@link #postProcessPropertyValues} implementation, and {@code pvs} otherwise.
	 * In a future version of this interface (with {@link #postProcessPropertyValues} removed),
	 * the default implementation will return the given {@code pvs} as-is directly.
	 * @param pvs the property values that the factory is about to apply (never {@code null})
	 * @param bean the bean instance created, but whose properties have not yet been set
	 * @param beanName the name of the bean
	 * @return the actual property values to apply to the given bean (can be the passed-in
	 * PropertyValues instance), or {@code null} which proceeds with the existing properties
	 * but specifically continues with a call to {@link #postProcessPropertyValues}
	 * (requiring initialized {@code PropertyDescriptor}s for the current bean class)
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @since 5.1
	 * @see #postProcessPropertyValues
	 *
	 *
	 * 对实例化后的bean处理bean的依赖
	 *
	 * 整个bean生命周期中，会被调用一次，发生在populateBean阶段，bean被实例化之后，applyPropertyValues属性注入之前，此时bean已经被实例化完成，
	 * 结合实现类调用实现类覆写了postProcessProperties的方法，对注解属性进行扫描注入
	 * 实现类：
	 * 	AutowiredAnnotationBeanPostProcessor#postProcessProperties：对Autowired、Value、Inject、Lookup注解进行注入
	 * 	CommonAnnotationBeanPostProcessor#postProcessProperties：对PostConstruct、PreDestroy、Resource、WebServiceRef、EJB注解进行注入
	 * 	ConfigurationClassPostProcessor#postProcessProperties：对Configuration、Bean注解进行注入
	 *  PersistenceAnnotationBeanPostProcessor#postProcessProperties：jpa包下，对PersistenceUnit、PersistenceContext注解进行注入
	 *
	 * 调用链：
	 * AbstractAutowireCapableBeanFactory # createBean
	 * |____AbstractAutowireCapableBeanFactory # doCreateBean
	 * 		|____AbstractAutowireCapableBeanFactory # populateBean
	 * 			 |____InstantiationAwareBeanPostProcessor # postProcessProperties
	 *
	 * 默认返回null
	 *
	 * <p>
	 *     对象属性后处理器
	 * </p>
	 */
	@Nullable
	default PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName)
			throws BeansException {

		return null;
	}

	/**
	 * Post-process the given property values before the factory applies them
	 * to the given bean. Allows for checking whether all dependencies have been
	 * satisfied, for example based on a "Required" annotation on bean property setters.
	 * <p>Also allows for replacing the property values to apply, typically through
	 * creating a new MutablePropertyValues instance based on the original PropertyValues,
	 * adding or removing specific values.
	 * <p>The default implementation returns the given {@code pvs} as-is.
	 * @param pvs the property values that the factory is about to apply (never {@code null})
	 * @param pds the relevant property descriptors for the target bean (with ignored
	 * dependency types - which the factory handles specifically - already filtered out)
	 * @param bean the bean instance created, but whose properties have not yet been set
	 * @param beanName the name of the bean
	 * @return the actual property values to apply to the given bean (can be the passed-in
	 * PropertyValues instance), or {@code null} to skip property population
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see #postProcessProperties
	 * @see org.springframework.beans.MutablePropertyValues
	 * @deprecated as of 5.1, in favor of {@link #postProcessProperties(PropertyValues, Object, String)}
	 *
	 * 整个bean生命周期中，会被调用一次，发生在populateBean阶段，bean被实例化之后，applyPropertyValues属性注入之前，此时bean已经被实例化完成，且经过了postProcessProperties，返回了非null的PropertyValues，才会调用这个方法
	 * 这个方法被标注为Deprecated是已经过时的方法，目的是PropertyValues应用到bean实列之前对对属性值进行拦截更改
	 * 调用链：
	 * AbstractAutowireCapableBeanFactory # createBean
	 * |____AbstractAutowireCapableBeanFactory # doCreateBean
	 * 		|____AbstractAutowireCapableBeanFactory # populateBean
	 * 			 |____InstantiationAwareBeanPostProcessor # postProcessPropertyValues
	 *
	 * 默认返回当前PropertyValues
	 */
	@Deprecated
	@Nullable
	default PropertyValues postProcessPropertyValues(
			PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {

		return pvs;
	}

}
