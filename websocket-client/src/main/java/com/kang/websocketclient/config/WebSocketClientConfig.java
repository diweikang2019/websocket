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
     * 启动一个websocket连接到预配置的url时可以考虑使用WebSocketConnectionManager。
     * WebSocketConnectionManager在指定uri，WebsocketClient和websocketHandler时通过start()和stop()方法连接到websocket服务器。
     * 如果setAutoStartup(boolean)设置为true，spring ApplicationContext刷新时会自动连接。
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
