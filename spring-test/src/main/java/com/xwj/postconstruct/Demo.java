package com.xwj.postconstruct;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Demo {

    public static void main(String[] args) {
//        BeanFactory beanFactory=new AnnotationConfigApplicationContext(MyPostConstructTest.class);

		MyAnnotationConfigApplicationContext myAnnotationConfigApplicationContext=new MyAnnotationConfigApplicationContext(MyPppDemo.class);
    }
}
