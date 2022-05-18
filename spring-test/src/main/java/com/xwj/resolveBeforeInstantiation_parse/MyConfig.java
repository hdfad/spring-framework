package com.xwj.resolveBeforeInstantiation_parse;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/05/18 15:09
 */
@Configuration
@Import(MyInstantiationAwareBeanPostProcessor.class)
public class MyConfig {
}
