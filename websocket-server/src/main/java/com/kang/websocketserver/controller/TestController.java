package com.kang.websocketserver.controller;

import com.kang.websocketserver.handler.WebSocketServerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/server", produces = {MediaType.APPLICATION_JSON_VALUE})
public class TestController {

    @Autowired
    WebSocketServerHandler webSocketServerHandler;

    @GetMapping(value = "/sendMessage")
    public String sendMessage() {
        webSocketServerHandler.sendMessage("001", "服务端与客户端信息通讯");
        return null;
    }

}