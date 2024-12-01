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
@ServerEndpoint("/ws/{paymentId}")
public class WebSocketEndpoint {
    private static WebSocketSessionManager sessionManager;

    @Autowired
    public void setWebSocketSessionManager(WebSocketSessionManager manager) {
        WebSocketEndpoint.sessionManager = manager;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("paymentId") Integer paymentId) {
        sessionManager.addSession("public", paymentId, session);
        System.out.println("New session opened for user " + paymentId + ": " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("paymentId") Integer paymentId) {
        System.out.println("Message from paymentId " + paymentId + ": " + message);
        sessionManager.sendToUser("public", paymentId, "Message for public group");
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
