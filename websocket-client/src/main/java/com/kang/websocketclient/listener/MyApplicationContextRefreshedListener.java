package com.kang.websocketclient.listener;

import com.kang.websocketclient.handler.WebSocketClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import javax.servlet.annotation.WebListener;

/**
 * spring容器初始化监听，初始化完成后执行websocket连接
 *
 * @author kesq
 */
@WebListener
@Component
public class MyApplicationContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    private static Logger logger = LoggerFactory.getLogger(MyApplicationContextRefreshedListener.class);

    @Autowired
    WebSocketClientHandler webSocketClientHandler;

    /**
     * 定义websocket配置
     *
     * @return
     */
    @Bean(name = "wsCloudConnectionManager")
    public WebSocketConnectionManager wsCloudConnectionManager() {
        StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketConnectionManager manager = new WebSocketConnectionManager(webSocketClient, webSocketClientHandler,
                "ws://localhost:8080/ws-service?shopId=001&appId=002");
        manager.setAutoStartup(true);
        return manager;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //TODO
        System.out.println("准备进行中");
    }
}
