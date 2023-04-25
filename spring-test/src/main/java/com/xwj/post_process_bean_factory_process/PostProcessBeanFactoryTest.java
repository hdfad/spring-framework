package com.xwj.post_process_bean_factory_process;

import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2023/04/24 17:08
 */
@Component
public class PostProcessBeanFactoryTest {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext(PostProcessBeanFactoryTest.class);
		ComponentAnnotation bean = annotationConfigApplicationContext.getBean(ComponentAnnotation.class);
		bean.print();
	}
}

@Configuration
@ComponentScan("com.xwj.post_process_bean_factory_process")
@Import(BeanAnnotation.class)
class Start{


}


