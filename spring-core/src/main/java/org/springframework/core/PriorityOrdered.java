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

package org.springframework.core;

/**
 * Extension of the {@link Ordered} interface, expressing a <em>priority</em>
 * ordering: {@code PriorityOrdered} objects are always applied before
 * <em>plain</em> {@link Ordered} objects regardless of their order values.
 *
 * <p>When sorting a set of {@code Ordered} objects, {@code PriorityOrdered}
 * objects and <em>plain</em> {@code Ordered} objects are effectively treated as
 * two separate subsets, with the set of {@code PriorityOrdered} objects preceding
 * the set of <em>plain</em> {@code Ordered} objects and with relative
 * ordering applied within those subsets.
 *
 * <p>This is primarily a special-purpose interface, used within the framework
 * itself for objects where it is particularly important to recognize
 * <em>prioritized</em> objects first, potentially without even obtaining the
 * remaining objects. A typical example: prioritized post-processors in a Spring
 * {@link org.springframework.context.ApplicationContext}.
 *
 * <p>Note: {@code PriorityOrdered} post-processor beans are initialized in
 * a special phase, ahead of other post-processor beans. This subtly
 * affects their autowiring behavior: they will only be autowired against
 * beans which do not require eager initialization for type matching.
 *
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 2.5
 * @see org.springframework.beans.factory.config.PropertyOverrideConfigurer
 * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
 *
 * 接口优先级排序，Ordered的子类
 *
 * 所有已知的实现类：
 * 	AutowiredAnnotationBeanPostProcessor，
	CommonAnnotationBeanPostProcessor会，
	ConfigurationClassPostProcessor，
	ExposeInvocationInterceptor，
	InitDestroyAnnotationBeanPostProcessor，
	PersistenceAnnotationBeanPostProcessor，
	PlaceholderConfigurerSupport，
	PreferencesPlaceholderConfigurer，
	PropertyOverrideConfigurer，
	PropertyPlaceholderConfigurer，
	PropertyResourceConfigurer，
	PropertySourcesPlaceholderConfigurer，
	RequiredAnnotationBeanPostProcessor
 *
 *
 * spring中提供了2个可以给bean排序的接口
 * @see org.springframework.core.Ordered
 * @see org.springframework.core.PriorityOrdered
 *
 * PriorityOrdered的优先级高于Ordered
 *
 * 若2个对象中有一个对象实现了PriorityOrdered接口，那么这个对象的优先级比Ordered更高。
 * 若2个对象都是PriorityOrdered或Ordered接口的实现类，那么比较Ordered接口的getOrder方法得到order值，值越低，优先级越高
 *
 */
public interface PriorityOrdered extends Ordered {
}
