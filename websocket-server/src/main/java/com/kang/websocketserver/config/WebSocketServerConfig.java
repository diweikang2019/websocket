package com.kang.websocketserver.config;

import com.kang.websocketserver.handler.WebSocketServerHandler;
import com.kang.websocketserver.interceptor.WebSocketHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

/**
 * @author weikang.di
 * @date 2022/4/26 5:05 PM
 */
@Configuration
@EnableWebSocket
public class WebSocketServerConfig implements WebSocketConfigurer {

    /**
     * 配置初始化注册通道
     *
     * @param registry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        try {
            // 注册通道
            registry.addHandler(webSocketServerHandler(), "/ws-service").setAllowedOrigins("*").addInterceptors(myInterceptor());
            // withSockJS() 方法声明我们想要使用 SockJS 功能，如果WebSocket不可用的话，会使用 SockJS；
            registry.addHandler(webSocketServerHandler(), "/sockjs/ws-service").setAllowedOrigins("*").addInterceptors(myInterceptor()).withSockJS();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("注册通道失败");
        }
    }

    /**
     * 配置消息处理
     *
     * @return
     */
    @Bean
    public WebSocketServerHandler webSocketServerHandler() {
        return new WebSocketServerHandler();
    }

    /**
     * websocket拦截器
     *
     * @return
     */
    @Bean
    public WebSocketHandshakeInterceptor myInterceptor() {
        return new WebSocketHandshakeInterceptor();
    }

    /**
     * 配置缓冲区的大小等配置信息
     *
     * @return
     */
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }

}
