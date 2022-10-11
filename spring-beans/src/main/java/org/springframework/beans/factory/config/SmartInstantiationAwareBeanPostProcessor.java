/*
 * Copyright 2002-2016 the original author or authors.
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

import java.lang.reflect.Constructor;

import org.springframework.beans.BeansException;
import org.springframework.lang.Nullable;

/**
 * Extension of the {@link InstantiationAwareBeanPostProcessor} interface,
 * adding a callback for predicting the eventual type of a processed bean.
 *
 * <p><b>NOTE:</b> This interface is a special purpose interface, mainly for
 * internal use within the framework. In general, application-provided
 * post-processors should simply implement the plain {@link BeanPostProcessor}
 * interface or derive from the {@link InstantiationAwareBeanPostProcessorAdapter}
 * class. New methods might be added to this interface even in point releases.
 *
 * @author Juergen Hoeller
 * @since 2.0.3
 * @see InstantiationAwareBeanPostProcessorAdapter
 *
 * SmartInstantiationAwareBeanPostProcessor后置处理器
 * 继承InstantiationAwareBeanPostProcessor，并未直接继承BeanPostProcessor
 * 整个SmartInstantiationAwareBeanPostProcessor后置处理器发生在doCreateBean阶段属性填充之前
 *
 */
public interface SmartInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessor {

	/**
	 * Predict the type of the bean to be eventually returned from this
	 * processor's {@link #postProcessBeforeInstantiation} callback.
	 * <p>The default implementation returns {@code null}.
	 * @param beanClass the raw class of the bean
	 * @param beanName the name of the bean
	 * @return the type of the bean, or {@code null} if not predictable
	 * @throws org.springframework.beans.BeansException in case of errors
	 *
	 * 预测Bean的类型，determineCandidateConstructors方法返回多个构造参数后需要通过predictBeanType选择合适的构造函数
	 * 调用链
	 *  AbstractBeanFactory # doGetBean
	 *  |____DefaultSingletonBeanRegistry # getSingleton
	 *  	 |____AbstractAutowireCapableBeanFactory # createBean
	 *  	 	  |____AbstractAutowireCapableBeanFactory # doCreateBean
	 *  	 	  	   |____AbstractAutowireCapableBeanFactory # createBeanInstance
	 *  	 	  	   		|____AbstractAutowireCapableBeanFactory # autowireConstructor
	 *  	 	  	   			 |____ConstructorResolver # createArgumentArray
	 *  	 	  	   			 	  |____ConstructorResolver # resolveAutowiredArgument
	 *  	 	  	   			 	  	   |____DefaultListableBeanFactory # resolveDependency
	 *  	 	  	   			 	  	   	    |____DefaultListableBeanFactory # doResolveDependency
	 *  	 	  	   			 	  	   	    	 |____DefaultListableBeanFactory # findAutowireCandidates
	 *  	 	  	   			 	  	   	    	 	  |____BeanFactoryUtils # beanNamesForTypeIncludingAncestors
	 *  	 	  	   			 	  	   	    	 	  	   |____DefaultListableBeanFactory # getBeanNamesForType
	 *  	 	  	   			 	  	   	    	 	  	   		|____DefaultListableBeanFactory # doGetBeanNamesForType
	 *  	 	  	   			 	  	   	    	 	  	   			 |____AbstractBeanFactory # isTypeMatch
	 *  	 	  	   			 	  	   	    	 	  	   			 	  |____AbstractAutowireCapableBeanFactory # predictBeanType
	 * 默认返回null
	 */
	@Nullable
	default Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
		return null;
	}

	/**
	 * Determine the candidate constructors to use for the given bean.
	 * <p>The default implementation returns {@code null}.
	 * @param beanClass the raw class of the bean (never {@code null})
	 * @param beanName the name of the bean
	 * @return the candidate constructors, or {@code null} if none specified
	 * @throws org.springframework.beans.BeansException in case of errors
	 *
	 * 整个bean创建的生命周期中只会创建一次，发生在createBeanInstance阶段，此时的bean处于未实例化阶段
	 * 目的是获取所有注入的构造函数，返回构造函数的数组
	 *
	 * 调用链
	 *  AbstractBeanFactory # doGetBean
	 *  |____DefaultSingletonBeanRegistry # getSingleton
	 *  	 |____AbstractAutowireCapableBeanFactory # createBean
	 *  	 	  |____AbstractAutowireCapableBeanFactory # doCreateBean
	 *  	 	  	   |____AbstractAutowireCapableBeanFactory # createBeanInstance
	 *  	 	  	   		|____AbstractAutowireCapableBeanFactory # determineConstructorsFromBeanPostProcessors
	 * 默认返回null
	 */
	@Nullable
	default Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName)
			throws BeansException {

		return null;
	}

	/**
	 * Obtain a reference for early access to the specified bean,
	 * typically for the purpose of resolving a circular reference.
	 * <p>This callback gives post-processors a chance to expose a wrapper
	 * early - that is, before the target bean instance is fully initialized.
	 * The exposed object should be equivalent to the what
	 * {@link #postProcessBeforeInitialization} / {@link #postProcessAfterInitialization}
	 * would expose otherwise. Note that the object returned by this method will
	 * be used as bean reference unless the post-processor returns a different
	 * wrapper from said post-process callbacks. In other words: Those post-process
	 * callbacks may either eventually expose the same reference or alternatively
	 * return the raw bean instance from those subsequent callbacks (if the wrapper
	 * for the affected bean has been built for a call to this method already,
	 * it will be exposes as final bean reference by default).
	 * <p>The default implementation returns the given {@code bean} as-is.
	 * @param bean the raw bean instance
	 * @param beanName the name of the bean
	 * @return the object to expose as bean reference
	 * (typically with the passed-in bean instance as default)
	 * @throws org.springframework.beans.BeansException in case of errors
	 *
	 * 获取提前引用对象，返回的对象是已实例化未初始化对象，如果存在代理则返回代理对象
	 *
	 * 调用链
	 *  AbstractBeanFactory # doGetBean
	 *  |____DefaultSingletonBeanRegistry # getSingleton
	 *  	 |____AbstractAutowireCapableBeanFactory # createBean
	 *  	 	  |____AbstractAutowireCapableBeanFactory # doCreateBean
	 *  	 	  	   |____AbstractAutowireCapableBeanFactory # getEarlyBeanReference
	 *
	 * 获取bean引用 默认返回传入的bean
	 */
	default Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
