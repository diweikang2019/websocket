package com.kang.websocketclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // 开启定时任务
@SpringBootApplication
public class WebsocketClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsocketClientApplication.class, args);
    }

}
