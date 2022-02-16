package com.xwj.publish_event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DemoEventPublisher {

    @Autowired
    private ApplicationContext applicationContext;

    public void pushlish(Object o){
        applicationContext.publishEvent(new MyApplicationEvent(this));
    }


}
