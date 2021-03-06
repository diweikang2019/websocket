package com.kang.websocketserver.handler;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author weikang.di
 * @date 2022/4/26 5:05 PM
 */
// @Component // 通过 @Bean 方式注入
public class WebSocketServerHandler extends AbstractWebSocketHandler {

    private final static Map<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println(session);
        String key = session.getAttributes().get("key").toString();
        System.out.println("key=" + key);
        if (SESSIONS.containsKey(key)) {
            WebSocketSession oldSession = SESSIONS.get(key);
            if (oldSession.isOpen()) {
                oldSession.close(CloseStatus.SERVICE_RESTARTED);
            }
            SESSIONS.remove(key);
            System.out.println("closed old connection and save new connection[" + key + "]");
        }
        System.out.println("new connection [" + key + "] successfully");
        SESSIONS.put(key, session);
        System.out.println("连接开启");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println(session);
        String key = session.getAttributes().get("key").toString();
        System.out.println("key=" + key);
        System.out.println("Closed code {" + closeStatus.getCode() + "}, reason {" + closeStatus.getReason() + "}");
        SESSIONS.remove(key);
        System.out.println("连接关闭");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println(session);
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
        System.out.println("Receive pong message, send pong message");
        session.sendMessage(new PongMessage(ByteBuffer.wrap("2".getBytes())));

        System.out.println("接受消息ID ===============   " + session.getId());
        System.out.println("接收到的消息===============   " + message);
        super.handlePongMessage(session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        System.out.println("出现异常了");
    }

    /**
     * 发送消息,如果连接断开，缓存到数据库
     *
     * @param key     客户端ID
     * @param content 发送内容
     */
    public void sendMessage(String key, String content) {
        System.out.println("send content>>> " + content);
        if (isConnected(key)) {
            try {
                SESSIONS.get(key).sendMessage(new TextMessage(content));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Send failure, connection [" + key + "] interrupt ");
        }
    }

    /**
     * 查询客户端是否在线
     *
     * @param key 客户端ID
     * @return
     */
    public static boolean isConnected(String key) {
        if (SESSIONS.containsKey(key)) {
            WebSocketSession session = SESSIONS.get(key);
            if (null != session && session.isOpen()) {
                return true;
            }
            System.out.println("connection[" + key + "] closed");
        } else {
            System.out.println("connection[" + key + "] not found");
        }
        return false;
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
