package com.kang.websocketclient.controller;

import com.kang.websocketclient.handler.WebSocketClientHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/client", produces = {MediaType.APPLICATION_JSON_VALUE})
public class TestController {

    @Autowired
    WebSocketClientHandler webSocketClientHandler;

    @GetMapping(value = "/sendMessage")
    public String sendMessage() {
        webSocketClientHandler.sendMessage("测试消息发送");
        return null;
    }

}