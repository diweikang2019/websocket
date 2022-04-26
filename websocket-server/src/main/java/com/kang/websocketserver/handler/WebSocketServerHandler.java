package com.kang.websocketserver.handler;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// @Component // 通过 @Bean 方式注入
public class WebSocketServerHandler extends AbstractWebSocketHandler {

    private final static Map<String, WebSocketSession> sessions = new ConcurrentHashMap<String, WebSocketSession>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println(session.getAttributes());
        String key = session.getAttributes().get("key").toString();
        System.out.println("key=" + key);
        if (sessions.containsKey(key)) {
            WebSocketSession oldSession = sessions.get(key);
            if (oldSession.isOpen()) {
                oldSession.close(CloseStatus.SERVICE_RESTARTED);
            }
            sessions.remove(key);
            System.out.println("closed old connection and save new connection[" + key + "]");
        }
        System.out.println("new connection [" + key + "] successfully");
        sessions.put(key, session);
        System.out.println("连接开启");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String key = session.getAttributes().get("key").toString();
        System.out.println("key=" + key);
        System.out.println("Closed code {" + closeStatus.getCode() + "}, reason {" + closeStatus.getReason() + "}");
        sessions.remove(key);
        System.out.println("连接关闭");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println("接收到来自客户端的消息");
        if (message instanceof PingMessage) {
            System.out.println("Receive ping message, send pong message");
            session.sendMessage(new PongMessage(ByteBuffer.wrap("2".getBytes())));
        } else if (message instanceof TextMessage) {
            System.out.println("Add message into queue");
        } else if (message instanceof BinaryMessage) {
            System.out.println("Receive binary message");
        } else {
            throw new Exception("Error format");
        }
        System.out.println("接受消息ID ===============   " + session.getId());
        System.out.println("接收到的消息===============   " + message);
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
                sessions.get(key).sendMessage(new TextMessage(content));
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
        if (sessions.containsKey(key)) {
            WebSocketSession session = sessions.get(key);
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
