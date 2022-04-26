package com.kang.websocketclient.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author weikang.di
 * @date 2022/4/26 5:05 PM
 */
// @Component // 通过 @Bean 方式注入
public class WebSocketClientHandler extends AbstractWebSocketHandler {

    /**
     * 当前服务建立与服务端建立的链接信息
     */
    private WebSocketSession clientSession = null;

    /**
     * 初始化当前重连次数
     */
    private Integer currentConnectionTimes = 0;

    @Autowired
    @Lazy
    @Qualifier("wsCloudConnectionManager")
    private WebSocketConnectionManager wsConnectionManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Websocket client connection established");
        this.clientSession = session;
        System.out.println("与服务端建立连接之后的session=" + clientSession);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("Websocket client connection closed");
        this.clientSession = null;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Receive text message, add message into queue");

        System.out.println("接受消息ID ===============   " + session.getId());
        System.out.println("接收到的消息===============   " + message);
        super.handleTextMessage(session, message);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        System.out.println("Receive binary message");

        System.out.println("接受消息ID ===============   " + session.getId());
        System.out.println("接收到的消息===============   " + message);
        super.handleBinaryMessage(session, message);
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        System.out.println("Receive pong message");

        System.out.println("接受消息ID ===============   " + session.getId());
        System.out.println("接收到的消息===============   " + message);
        super.handlePongMessage(session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        exception.printStackTrace();
        if (session.isOpen()) {
            session.close();
        }
    }

    /**
     * 发送消息，如果连接断开，缓存到数据库
     *
     * @param content 发送内容
     */
    public void sendMessage(String content) {
        System.out.println("send content >>> " + content);
        if (isConnected()) {
            try {
                this.clientSession.sendMessage(new TextMessage(content));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Connection closed ");
            System.out.println("Connection interrupt ");
        }
    }

    /**
     * 判断连接是否存在
     *
     * @return
     */
    public Boolean isConnected() {
        return null != this.clientSession && this.clientSession.isOpen();
    }

    /**
     * 每30秒发送一个心跳，检测断开后重连
     */
    @Scheduled(cron = "${websocket.pong.schedule.cron}")
    public void heartBeat() {
        System.out.println("执行定时任务开始");
        System.out.println(isConnected());
        try {
            if (isConnected()) {
                this.clientSession.sendMessage(new PongMessage(ByteBuffer.wrap("1".getBytes())));
            } else {
                System.out.println("Send Ping Message fail, not connect ");
                System.out.println("try " + currentConnectionTimes + " times, connection fail, reconnecting");
                currentConnectionTimes++;
                // 重连
                wsConnectionManager.startInternal();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
