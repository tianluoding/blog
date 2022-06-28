package com.blog.service.impl;

import com.blog.service.WebSocketService;
import com.blog.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class WebSocketServiceImpl implements WebSocketService {
    @Autowired
    private WebSocketServer webSocketServer;
    @Override
    @Async
    public void sendInfo(String msg) {
        webSocketServer.sendInfo(msg);
    }
}
