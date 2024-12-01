package com.henrytran1803.BEBakeManage.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/ws/{userId}")
public class WebSocketEndpoint {
    private static WebSocketSessionManager sessionManager;

    @Autowired
    public void setWebSocketSessionManager(WebSocketSessionManager manager) {
        WebSocketEndpoint.sessionManager = manager;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId) {
        sessionManager.addSession("public", userId, session);
        System.out.println("New session opened for user " + userId + ": " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("userId") Integer userId) {
        System.out.println("Message from user " + userId + ": " + message);
        sessionManager.sendToUser("public", userId, "Message for public group");
    }

    @OnClose
    public void onClose(Session session) {
        sessionManager.removeSession(session);
        System.out.println("Session closed: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("Error in session " + session.getId());
        throwable.printStackTrace();
    }
}
