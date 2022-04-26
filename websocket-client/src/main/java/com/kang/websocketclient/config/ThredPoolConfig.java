package com.kang.websocketclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 线程池
 *
 * @author weikang.di
 * @date 2022/4/26 5:05 PM
 */
@Configuration
public class ThredPoolConfig {

    @Bean
    public ScheduledThreadPoolExecutor scheduledExecutorService() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);
        return executor;
    }
}