/*
 * Copyright 2002-2021 the original author or authors.
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

package org.springframework.context.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.metrics.ApplicationStartup;
import org.springframework.core.metrics.StartupStep;
import org.springframework.lang.Nullable;

/**
 * Delegate for AbstractApplicationContext's post-processor handling.
 *
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 4.0
 */
final class PostProcessorRegistrationDelegate {

	private PostProcessorRegistrationDelegate() {
	}


	/**
	 * 处理2部分：
	 * 			BeanDefinitionRegistryPostProcessor
	 * 			BeanFactoryPostProcessor
	 * @param beanFactory
	 * @param beanFactoryPostProcessors
	 */
	public static void invokeBeanFactoryPostProcessors(
			ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {

		// WARNING: Although it may appear that the body of this method can be easily
		// refactored to avoid the use of multiple loops and multiple lists, the use
		// of multiple lists and multiple passes over the names of processors is
		// intentional. We must ensure that we honor the contracts for PriorityOrdered
		// and Ordered processors. Specifically, we must NOT cause processors to be
		// instantiated (via getBean() invocations) or registered in the ApplicationContext
		// in the wrong order.
		//
		// Before submitting a pull request (PR) to change this method, please review the
		// list of all declined PRs involving changes to PostProcessorRegistrationDelegate
		// to ensure that your proposal does not result in a breaking change:
		// https://github.com/spring-projects/spring-framework/issues?q=PostProcessorRegistrationDelegate+is%3Aclosed+label%3A%22status%3A+declined%22

		// Invoke BeanDefinitionRegistryPostProcessors first, if any.
		//已经被处理过的bean
		Set<String> processedBeans = new HashSet<>();

		/**
		 * beanFactory->ConfigurableListableBeanFactory,
		 * DefaultListableBeanFactory是ConfigurableListableBeanFactory子类，整个生命周期中默认bean工厂就是DefaultListableBeanFactory
		 * DefaultListableBeanFactory实现BeanDefinitionRegistry
		 * 所以这个instanceof是能匹配到类型的
		 */
		if (beanFactory instanceof BeanDefinitionRegistry) {
			BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
			//BeanFactoryPostProcessor--非BeanDefinitionRegistryPostProcessor类型则
			List<BeanFactoryPostProcessor> regularPostProcessors = new ArrayList<>();
			//BeanDefinitionRegistryPostProcessor
			List<BeanDefinitionRegistryPostProcessor> registryProcessors = new ArrayList<>();

			//遍历所有的beanFactory，如果是BeanDefinitionRegistryPostProcessor类型，则属于需要注册后置处理器，否则添加到BeanFactoryPostProcessor中
			for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors) {
				if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
					BeanDefinitionRegistryPostProcessor registryProcessor =(BeanDefinitionRegistryPostProcessor) postProcessor;
					/*
					 * 第一阶段：执行BeanDefinitionRegistryPostProcessor接口的postProcessBeanDefinitionRegistry方法
					 * 对配置bean进行装载，解析配置bean中的@Configuration、@Component、@PropertySource、@ComponentScan、@ImportResource、@Bean
					 * 如何解决@Bean中的别名映射呢？
					 * 		使用容器ConcurrentHashMap对别名存储，key为上一个别名，value为当前别名
					 */
					registryProcessor.postProcessBeanDefinitionRegistry(registry);
					//  添加到registryProcessors(用于最后执行postProcessBeanFactory方法)
					registryProcessors.add(registryProcessor);
				}
				else {
					//非BeanDefinitionRegistryPostProcessor类型则 添加到regularPostProcessors中
					regularPostProcessors.add(postProcessor);
				}
			}

			//=====================================================
			//上面执行的是 BeanDefinitionRegistryPostProcessor
			//下面开始执行 BeanFactoryPostProcessor
			//=====================================================
			System.out.println("============================================================");
			System.out.println("=====    执行结束BeanDefinitionRegistryPostProcessor     =====");
			System.out.println("-------------------------------------------------------------");
			System.out.println("=====          开始执行BeanFactoryPostProcessor          =====");
			System.out.println("=============================================================");
			// Do not initialize FactoryBeans here: We need to leave all regular beans
			// uninitialized to let the bean factory post-processors apply to them!
			// Separate between BeanDefinitionRegistryPostProcessors that implement
			// PriorityOrdered, Ordered, and the rest.

			/*根据实现PriorityOrdered或者Ordered接口按照顺序进行处理，PriorityOrdered.getOrder() asc,Ordered.getOrder() asc*/
			//currentRegistryProcessors：当前需要处理的后置处理器BeanDefinitionRegistryPostProcessor
			List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<>();

			// First, invoke the BeanDefinitionRegistryPostProcessors that implement PriorityOrdered.
			//首先，调用实现 PriorityOrdered 的 BeanDefinitionRegistryPostProcessors
			//获取所有BeanDefinitionRegistryPostProcessor的实现类，
			String[] postProcessorNames =
					beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
			/*
			 *
			 * 循环所有的postProcessorName，判断类型是否实现了PriorityOrdered，对于实现了PriorityOrdered的类实例化优先级要高于Order接口
			 * 所以首先会获取实现了PriorityOrdered接口的类，进行排序然后调用invokeBeanDefinitionRegistryPostProcessors对
			 *
			 * */
			for (String ppName : postProcessorNames) {
				if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
					//根据postProcessorName获取当前的BeanDefinitionRegistryPostProcessor，添加到currentRegistryProcessors中
					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
					processedBeans.add(ppName);//添加到processedBeans中，标识当前BeanDefinitionRegistryPostProcessor已经被处理
				}
			}

			//排序currentRegistryProcessors(根据是否实现PriorityOrdered、Ordered接口和order值来排序)
			//对已经实现了PriorityOrder的BeanDefinitionRegistryPostProcessor进行排序，此处的是实现了PriorityOrdered的接口
			sortPostProcessors(currentRegistryProcessors, beanFactory);

			//将当前的需要处理的后置处理器BeanDefinitionRegistryPostProcessor添加到BeanDefinitionRegistryPostProcessor中
			registryProcessors.addAll(currentRegistryProcessors);
			//执行BeanDefinitionRegistry后置处理器，调用BeanDefinitionRegistryPostProcessor的postProcessBeanDefinitionRegistry方法处理当前PriorityOrdered的实现接口，
			//注册配置类，对配置类中的注解进行装载，处理@Bean别名信息
			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry, beanFactory.getApplicationStartup());

			//在调用完清空currentRegistryProcessors list.clear()
			currentRegistryProcessors.clear();



			// Next, invoke the BeanDefinitionRegistryPostProcessors that implement Ordered.
			//然后，调用已经实现了Ordered的BeanDefinitionRegistryPostProcessors
			postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
			/*
			 * 同理，首先遍历所有的postProcessorName，找出所有未被处理的BeanDefinitionRegistryPostProcessor实现类&&类型为Ordered实现类的BeanDefinitionRegistryPostProcessor
			 * 并添加到currentRegistryProcessors容器中
			 * 并且添加到processedBeans中，标识当前BeanDefinitionRegistryPostProcessor已经被处理
			 * */
			for (String ppName : postProcessorNames) {
				if (!processedBeans.contains(ppName) && beanFactory.isTypeMatch(ppName, Ordered.class)) {
					//currentRegistryProcessors
					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
					processedBeans.add(ppName);
				}
			}
			//对所有的Order实现的BeanDefinitionRegistryPostProcessor进行排序
			sortPostProcessors(currentRegistryProcessors, beanFactory);
			//将当前处理的BeanDefinitionRegistryPostProcessor添加到registryProcessors
			registryProcessors.addAll(currentRegistryProcessors);
			//执行BeanDefinitionRegistry后置处理器，调用BeanDefinitionRegistryPostProcessor的postProcessBeanDefinitionRegistry方法处理当前的currentRegistryProcessors
			//注册配置类，对配置类中的注解进行装载，处理@Bean别名信息
			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry, beanFactory.getApplicationStartup());
			//clear
			currentRegistryProcessors.clear();

			// Finally, invoke all other BeanDefinitionRegistryPostProcessors until no further ones appear.
			//最后，调用所有其他 BeanDefinitionRegistryPostProcessor 直到不再出现
			boolean reiterate = true;
			while (reiterate) {
				reiterate = false;
				//获取所有的BeanDefinitionRegistryPostProcessor，筛选条件一样
				postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
				//遍历所有的postProcessorNames
				for (String ppName : postProcessorNames) {
					//如果当前的postProcessorName没有存在于processedBeans中，即：当前的BeanDefinitionRegistryPostProcessor还没被处理
					if (!processedBeans.contains(ppName)) {
						//将当前的BeanDefinitionRegistryPostProcessor添加到currentRegistryProcessors中
						currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
						//标识已经被添加处理
						processedBeans.add(ppName);
						reiterate = true;
					}
				}
				//排序，所有剩下的
				sortPostProcessors(currentRegistryProcessors, beanFactory);
				//添加到registryProcessors中
				registryProcessors.addAll(currentRegistryProcessors);
				//执行BeanDefinitionRegistry后置处理器，调用BeanDefinitionRegistryPostProcessor的postProcessBeanDefinitionRegistry方法处理当前的currentRegistryProcessors
				//注册配置类，对配置类中的注解进行装载，处理@Bean别名信息
				invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry, beanFactory.getApplicationStartup());
				//clear
				currentRegistryProcessors.clear();
			}

			// Now, invoke the postProcessBeanFactory callback of all processors handled so far.
			//处理所有的registryProcessors，按照添加顺序先对PriorityOrdered处理，再处理order、最后处理其他的
			invokeBeanFactoryPostProcessors(registryProcessors, beanFactory);
			//再处理非BeanDefinitionRegistryPostProcessor类型
			invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);
		}

		else {
			// Invoke factory processors registered with the context instance.
			//如果非BeanDefinitionRegistry类型，则直接调用invokeBeanFactoryPostProcessors处理
			invokeBeanFactoryPostProcessors(beanFactoryPostProcessors, beanFactory);
		}

		// Do not initialize FactoryBeans here: We need to leave all regular beans
		// uninitialized to let the bean factory post-processors apply to them!
		//获取所有的BeanFactoryPostProcessor实现类
		String[] postProcessorNames =
				beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);

		// Separate between BeanFactoryPostProcessors that implement PriorityOrdered,
		// Ordered, and the rest.
		//priorityOrderedPostProcessors：priorityOrdered实现类的后置处理器集合
		List<BeanFactoryPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
		//orderedPostProcessorNames：order实现类的后置处理器集合
		List<String> orderedPostProcessorNames = new ArrayList<>();
		//什么都不是的普通后置处理器
		List<String> nonOrderedPostProcessorNames = new ArrayList<>();
		//遍历所有的BeanFactoryPostProcessor实现类
		for (String ppName : postProcessorNames) {
			if (processedBeans.contains(ppName)) {
				// skip - already processed in first phase above
			}
			//对类型是PriorityOrdered的后置处理器添加到priorityOrderedPostProcessors
			else if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
				priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanFactoryPostProcessor.class));
			}
			//对类型是Ordered的后置处理器添加到orderedPostProcessorNames中
			else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
				orderedPostProcessorNames.add(ppName);
			}
			else {
				//剩下的添加到nonOrderedPostProcessorNames
				nonOrderedPostProcessorNames.add(ppName);
			}
		}

		// First, invoke the BeanFactoryPostProcessors that implement PriorityOrdered.
		//对priorityOrderedPostProcessors类中的BeanFactoryPostProcessor排序
		sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
		//调用invokeBeanFactoryPostProcessors处理priorityOrderedPostProcessors的子类
		invokeBeanFactoryPostProcessors(priorityOrderedPostProcessors, beanFactory);

		// Next, invoke the BeanFactoryPostProcessors that implement Ordered.
		List<BeanFactoryPostProcessor> orderedPostProcessors = new ArrayList<>(orderedPostProcessorNames.size());
		//遍历所有的orderedPostProcessorNames，将orderedPostProcessorNames对应的BeanFactoryPostProcessor对象拷贝到orderedPostProcessors中
		for (String postProcessorName : orderedPostProcessorNames) {
			orderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		//排序所有的orderedPostProcessors
		sortPostProcessors(orderedPostProcessors, beanFactory);
		//调用invokeBeanFactoryPostProcessors执行orderedPostProcessors的实现类
		invokeBeanFactoryPostProcessors(orderedPostProcessors, beanFactory);

		// Finally, invoke all other BeanFactoryPostProcessors.
		/*调用什么排序都没实现的BeanFactoryPostProcessor*/
		List<BeanFactoryPostProcessor> nonOrderedPostProcessors = new ArrayList<>(nonOrderedPostProcessorNames.size());
		for (String postProcessorName : nonOrderedPostProcessorNames) {
			nonOrderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		invokeBeanFactoryPostProcessors(nonOrderedPostProcessors, beanFactory);

		// Clear cached merged bean definitions since the post-processors might have
		// modified the original metadata, e.g. replacing placeholders in values...
		// 清除元数据缓存（mergedBeanDefinitions、allBeanNamesByType、singletonBeanNamesByType），
		// 因为后处理器可能已经修改了原始元数据，例如， 替换值中的占位符
		beanFactory.clearMetadataCache();
	}

	/**
	 * 注册时，PriorityOrdered顺序优先于Ordered
	 * @param beanFactory
	 * @param applicationContext
	 */
	public static void registerBeanPostProcessors(
			ConfigurableListableBeanFactory beanFactory, AbstractApplicationContext applicationContext) {

		// WARNING: Although it may appear that the body of this method can be easily
		// refactored to avoid the use of multiple loops and multiple lists, the use
		// of multiple lists and multiple passes over the names of processors is
		// intentional. We must ensure that we honor the contracts for PriorityOrdered
		// and Ordered processors. Specifically, we must NOT cause processors to be
		// instantiated (via getBean() invocations) or registered in the ApplicationContext
		// in the wrong order.
		//
		// Before submitting a pull request (PR) to change this method, please review the
		// list of all declined PRs involving changes to PostProcessorRegistrationDelegate
		// to ensure that your proposal does not result in a breaking change:
		// https://github.com/spring-projects/spring-framework/issues?q=PostProcessorRegistrationDelegate+is%3Aclosed+label%3A%22status%3A+declined%22

		/**
		 * 根据类型获取所有BeanPostProcessor的子类类名，返回数组
		 */
		String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);

		// Register BeanPostProcessorChecker that logs an info message when
		// a bean is created during BeanPostProcessor instantiation, i.e. when
		// a bean is not eligible for getting processed by all BeanPostProcessors.
		// 实现了BeanPostProcessor的类的数量
		int beanProcessorTargetCount = beanFactory.getBeanPostProcessorCount() + 1 + postProcessorNames.length;
		//移除旧的beanFactory，添加新的
		beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory, beanProcessorTargetCount));

		// Separate between BeanPostProcessors that implement PriorityOrdered,
		// Ordered, and the rest.
		//判断所有BeanPostProcessor实现了那个接口包括PriorityOrdered、Ordered还是其他的，不同的实现添加到BeanPostProcessor顺序不同，
		// 先添加PriorityOrdered，再添加Ordered、最后添加剩下的
		//transformedBeanName解析name
		List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
		List<BeanPostProcessor> internalPostProcessors = new ArrayList<>();
		List<String> orderedPostProcessorNames = new ArrayList<>();
		List<String> nonOrderedPostProcessorNames = new ArrayList<>();
		for (String ppName : postProcessorNames) {
			//通过类型匹配到PriorityOrdered的子类
			if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
				BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
				priorityOrderedPostProcessors.add(pp);
				if (pp instanceof MergedBeanDefinitionPostProcessor) {
					internalPostProcessors.add(pp);
				}
			}
			//通过类型匹配到Ordered的子类
			else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
				orderedPostProcessorNames.add(ppName);
			}
			else {
				nonOrderedPostProcessorNames.add(ppName);
			}
		}

		// First, register the BeanPostProcessors that implement PriorityOrdered.
		sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
		registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors);

		// Next, register the BeanPostProcessors that implement Ordered.
		List<BeanPostProcessor> orderedPostProcessors = new ArrayList<>(orderedPostProcessorNames.size());
		for (String ppName : orderedPostProcessorNames) {
			BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
			orderedPostProcessors.add(pp);
			if (pp instanceof MergedBeanDefinitionPostProcessor) {
				internalPostProcessors.add(pp);
			}
		}
		sortPostProcessors(orderedPostProcessors, beanFactory);
		registerBeanPostProcessors(beanFactory, orderedPostProcessors);

		// Now, register all regular BeanPostProcessors.
		List<BeanPostProcessor> nonOrderedPostProcessors = new ArrayList<>(nonOrderedPostProcessorNames.size());
		for (String ppName : nonOrderedPostProcessorNames) {
			BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
			nonOrderedPostProcessors.add(pp);
			if (pp instanceof MergedBeanDefinitionPostProcessor) {
				internalPostProcessors.add(pp);
			}
		}
		registerBeanPostProcessors(beanFactory, nonOrderedPostProcessors);

		// Finally, re-register all internal BeanPostProcessors.
		sortPostProcessors(internalPostProcessors, beanFactory);
		registerBeanPostProcessors(beanFactory, internalPostProcessors);

		// Re-register post-processor for detecting inner beans as ApplicationListeners,
		// moving it to the end of the processor chain (for picking up proxies etc).
		beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(applicationContext));
	}

	private static void sortPostProcessors(List<?> postProcessors, ConfigurableListableBeanFactory beanFactory) {
		// Nothing to sort?
		if (postProcessors.size() <= 1) {
			return;
		}
		Comparator<Object> comparatorToUse = null;
		if (beanFactory instanceof DefaultListableBeanFactory) {
			comparatorToUse = ((DefaultListableBeanFactory) beanFactory).getDependencyComparator();
		}
		if (comparatorToUse == null) {
			comparatorToUse = OrderComparator.INSTANCE;
		}
		postProcessors.sort(comparatorToUse);
	}

	/**
	 * Invoke the given BeanDefinitionRegistryPostProcessor beans.
	 * 调用BeanDefinitionRegistryPostProcessor的postProcessBeanDefinitionRegistry方法
	 */
	private static void invokeBeanDefinitionRegistryPostProcessors(
			Collection<? extends BeanDefinitionRegistryPostProcessor> postProcessors, BeanDefinitionRegistry registry, ApplicationStartup applicationStartup) {

		/*
		 *
		 * 遍历所有的postProcessor集合，执行postProcessBeanDefinitionRegistry方法，装载配置类注解，映射@Bean别名信息
		 * */
		for (BeanDefinitionRegistryPostProcessor postProcessor : postProcessors) {
			StartupStep postProcessBeanDefRegistry = applicationStartup.start("spring.context.beandef-registry.post-process")
					.tag("postProcessor", postProcessor::toString);
			//注册配置类，对配置类中的注解进行装载，处理@Bean别名信息
			postProcessor.postProcessBeanDefinitionRegistry(registry);
			postProcessBeanDefRegistry.end();
		}
	}

	/**
	 * Invoke the given BeanFactoryPostProcessor beans.
	 * 通过调用postProcessBeanFactory进行扩展处理，此时的bean已经完成注册，调用时按照PriorityOrdered、Ordered顺序来进行调用
	 * 在此postProcessBeanFactory中可以修改bean定义信息何对bean定义信息进行补充
	 */
	private static void invokeBeanFactoryPostProcessors(
			Collection<? extends BeanFactoryPostProcessor> postProcessors, ConfigurableListableBeanFactory beanFactory) {

		for (BeanFactoryPostProcessor postProcessor : postProcessors) {
			StartupStep postProcessBeanFactory = beanFactory.getApplicationStartup().start("spring.context.bean-factory.post-process")
					.tag("postProcessor", postProcessor::toString);
			postProcessor.postProcessBeanFactory(beanFactory);
			postProcessBeanFactory.end();
		}
	}

	/**
	 * Register the given BeanPostProcessor beans.
	 */
	private static void registerBeanPostProcessors(
			ConfigurableListableBeanFactory beanFactory, List<BeanPostProcessor> postProcessors) {

		if (beanFactory instanceof AbstractBeanFactory) {
			// Bulk addition is more efficient against our CopyOnWriteArrayList there
			((AbstractBeanFactory) beanFactory).addBeanPostProcessors(postProcessors);
		}
		else {
			for (BeanPostProcessor postProcessor : postProcessors) {
				beanFactory.addBeanPostProcessor(postProcessor);
			}
		}
	}


	/**
	 * BeanPostProcessor that logs an info message when a bean is created during
	 * BeanPostProcessor instantiation, i.e. when a bean is not eligible for
	 * getting processed by all BeanPostProcessors.
	 */
	private static final class BeanPostProcessorChecker implements BeanPostProcessor {

		private static final Log logger = LogFactory.getLog(BeanPostProcessorChecker.class);

		private final ConfigurableListableBeanFactory beanFactory;

		private final int beanPostProcessorTargetCount;

		public BeanPostProcessorChecker(ConfigurableListableBeanFactory beanFactory, int beanPostProcessorTargetCount) {
			this.beanFactory = beanFactory;
			this.beanPostProcessorTargetCount = beanPostProcessorTargetCount;
		}

		@Override
		public Object postProcessBeforeInitialization(Object bean, String beanName) {
			return bean;
		}

		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName) {
			if (!(bean instanceof BeanPostProcessor) && !isInfrastructureBean(beanName) &&
					this.beanFactory.getBeanPostProcessorCount() < this.beanPostProcessorTargetCount) {
				if (logger.isInfoEnabled()) {
					logger.info("Bean '" + beanName + "' of type [" + bean.getClass().getName() +
							"] is not eligible for getting processed by all BeanPostProcessors " +
							"(for example: not eligible for auto-proxying)");
				}
			}
			return bean;
		}

		private boolean isInfrastructureBean(@Nullable String beanName) {
			if (beanName != null && this.beanFactory.containsBeanDefinition(beanName)) {
				BeanDefinition bd = this.beanFactory.getBeanDefinition(beanName);
				return (bd.getRole() == RootBeanDefinition.ROLE_INFRASTRUCTURE);
			}
			return false;
		}
	}

}
