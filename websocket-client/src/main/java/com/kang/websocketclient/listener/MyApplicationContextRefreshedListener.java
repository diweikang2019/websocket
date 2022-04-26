package com.kang.websocketclient.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.servlet.annotation.WebListener;

/**
 * spring容器初始化监听，初始化完成后执行websocket连接
 *
 * @author kesq
 */
@WebListener
@Component
public class MyApplicationContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("准备进行中");
    }
}
