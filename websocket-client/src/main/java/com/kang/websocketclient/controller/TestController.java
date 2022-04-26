package com.kang.websocketclient.controller;

import com.kang.websocketclient.handler.WebSocketClientHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weikang.di
 * @date 2022/4/26 5:05 PM
 */
@RestController
@RequestMapping(value = "/api/client", produces = {MediaType.APPLICATION_JSON_VALUE})
public class TestController {

    @Autowired
    private WebSocketClientHandler webSocketClientHandler;

    @GetMapping(value = "/sendMessage")
    public String sendMessage() {
        webSocketClientHandler.sendMessage("测试客户端消息发送");
        return null;
    }

}