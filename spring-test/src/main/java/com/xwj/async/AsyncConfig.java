package com.xwj.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author buming
 * Email buming@uoko.com
 * @Date: 2022/03/28 11:20
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "pay-callback-task")
    public Executor asyncServiceExecutor() {
		System.out.println("开启异步,获取系统核心数:"+Runtime.getRuntime().availableProcessors());;
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数
        executor.setCorePoolSize(1);
        //最大线程数
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        //队列大小
        executor.setQueueCapacity(30);
        //线程名称前缀
        executor.setThreadNamePrefix("pay-callback-task-");
        //时间
        executor.setKeepAliveSeconds(60);
        // main 线程处理
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
		System.out.println("开启成功");
        return executor;
    }
}
