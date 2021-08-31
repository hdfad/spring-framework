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
 * {@code Ordered} is an interface that can be implemented by objects that
 * should be <em>orderable</em>, for example in a {@code Collection}.
 *
 * <p>The actual {@link #getOrder() order} can be interpreted as prioritization,
 * with the first object (with the lowest order value) having the highest
 * priority.
 *
 * <p>Note that there is also a <em>priority</em> marker for this interface:
 * {@link PriorityOrdered}. Consult the Javadoc for {@code PriorityOrdered} for
 * details on how {@code PriorityOrdered} objects are ordered relative to
 * <em>plain</em> {@link Ordered} objects.
 *
 * <p>Consult the Javadoc for {@link OrderComparator} for details on the
 * sort semantics for non-ordered objects.
 *
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 07.04.2003
 * @see PriorityOrdered
 * @see OrderComparator
 * @see org.springframework.core.annotation.Order
 * @see org.springframework.core.annotation.AnnotationAwareOrderComparator
 *
 *
 * Spring是一个大量使用策略设计模式的框架，这意味着有很多相同接口的实现类，那么必定会有优先级的问题，spring为了解决排序问题，提供了Ordered接口
 *
 *   spring中提供了2个可以给bean排序的接口
 *   @see org.springframework.core.Ordered
 *   @see org.springframework.core.PriorityOrdered
 *  不决定Bean的加载顺序和实例化顺序，只决定Bean的执行顺序
 *  若2个对象中有一个对象实现了PriorityOrdered接口，那么这个对象的优先级比Ordered更高。
 *  若2个对象都是PriorityOrdered或Ordered接口的实现类，那么比较Ordered接口的getOrder方法得到order值，值越低，优先级越高
 */
public interface Ordered {

	/**
	 * Useful constant for the highest precedence value.
	 * @see java.lang.Integer#MIN_VALUE
	 * 最高级(数值最小)
	 */
	int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

	/**
	 * Useful constant for the lowest precedence value.
	 * @see java.lang.Integer#MAX_VALUE
	 * 最低级(数值最大)
	 */
	int LOWEST_PRECEDENCE = Integer.MAX_VALUE;


	/**
	 * Get the order value of this object.
	 * <p>Higher values are interpreted as lower priority. As a consequence,
	 * the object with the lowest value has the highest priority (somewhat
	 * analogous to Servlet {@code load-on-startup} values).
	 * <p>Same order values will result in arbitrary sort positions for the
	 * affected objects.
	 * @return the order value
	 * @see #HIGHEST_PRECEDENCE
	 * @see #LOWEST_PRECEDENCE
	 * 获取排序值 order , 值越小,优先级越高
	 */
	int getOrder();

}
