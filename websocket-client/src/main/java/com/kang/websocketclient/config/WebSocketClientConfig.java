package com.kang.websocketclient.config;

import com.kang.websocketclient.handler.WebSocketClientHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

/**
 * @author weikang.di
 * @date 2022/4/26 5:05 PM
 */
@Configuration
public class WebSocketClientConfig {

    /**
     * 定义websocket配置
     *
     * @return
     */
    @Bean(name = "wsCloudConnectionManager")
    public WebSocketConnectionManager wsCloudConnectionManager() {
        StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketConnectionManager manager = new WebSocketConnectionManager(webSocketClient, webSocketClientHandler(),
                "ws://localhost:8087/ws-service?key=001&userId=48MSIDH8");
        manager.setAutoStartup(true);
        return manager;
    }

    /**
     * 配置消息处理
     *
     * @return
     */
    @Bean
    public WebSocketClientHandler webSocketClientHandler() {
        return new WebSocketClientHandler();
    }
}
