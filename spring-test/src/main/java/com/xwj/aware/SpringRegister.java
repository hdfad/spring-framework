package com.xwj.aware;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public class SpringRegister implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return SpringRegister.applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringRegister.applicationContext = applicationContext;

    }

    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> var2) throws BeansException {
        return applicationContext.getBean(name, var2);
    }

    public static <T> T getBean(Class<T> aClass) throws BeansException {
        return applicationContext.getBean(aClass);
    }

    public static <T> T getBean(Class<T> aClass, Object... objects) {
        return applicationContext.getBean(aClass, objects);
    }

    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

}
