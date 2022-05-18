package com.xwj.autowiremode;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
 
/**
 * 调用consumer的providerSout方法，
 * 如果已经成功注入必然可以打印出对象的hash值
 */
public class MainTest {
 
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        Consumer consumer = context.getBean(Consumer.class);
        consumer.providerSout();
        System.err.println(context.getBean(Providera.class));
        System.err.println(context.getBean(Providerb.class));
        context.close();
    }
}