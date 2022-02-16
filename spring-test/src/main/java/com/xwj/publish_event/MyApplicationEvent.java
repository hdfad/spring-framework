package com.xwj.publish_event;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/02/15 10:24
 */
public class MyApplicationEvent extends ApplicationEvent {

    public MyApplicationEvent(Object source) {
        super(source);
    }

    public MyApplicationEvent(Object source, Clock clock) {
        super(source, clock);
    }
    String msg;
    public MyApplicationEvent(Object source,String msg) {
        super(source);
        this.msg=msg;
    }
}
