package com.xwj.publish_event;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * 此处调用是因为aware接口被调用
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/02/15 10:46
 */
//@Component
@Deprecated
public class Push implements ApplicationContextAware {
    ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext.publishEvent(new MyApplicationEvent(applicationContext,"ddddd"));
        applicationContext.publishEvent(new MyApplicationEvent(applicationContext,"2222"));
        applicationContext.publishEvent(new MyApplicationEvent(applicationContext,"3333"));
        applicationContext.publishEvent(new MyApplicationEvent(applicationContext,"444"));
        applicationContext.publishEvent(new MyApplicationEvent(applicationContext,"555"));
        applicationContext.publishEvent(new MyApplicationEvent(applicationContext,"666"));
        applicationContext.publishEvent(new MyApplicationEvent(applicationContext,"777"));
    }

    public Push(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
