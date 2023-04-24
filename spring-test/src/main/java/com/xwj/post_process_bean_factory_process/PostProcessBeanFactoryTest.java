package com.xwj.post_process_bean_factory_process;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2023/04/24 17:08
 */
@ComponentScan("com.xwj.post_process_bean_factory_process")
@Import(BeanAnnotation.class)
public class PostProcessBeanFactoryTest {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext(PostProcessBeanFactoryTest.class);
		BeanAnnotation bean = annotationConfigApplicationContext.getBean(BeanAnnotation.class);
		bean.find();
	}
}



