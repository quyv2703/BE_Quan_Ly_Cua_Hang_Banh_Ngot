package com.henrytran1803.BEBakeManage.websocket;

import jakarta.websocket.Session;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;


@Component
public class WebSocketSessionManager {
    private final Map<String, Map<Integer, Session>> groupSessions = new ConcurrentHashMap<>();

    public void addSession(String group, Integer userId, Session session) {
        groupSessions.computeIfAbsent(group, k -> new ConcurrentHashMap<>()).put(userId, session);
    }

    public void removeSession(Session session) {
        groupSessions.forEach((group, sessions) ->
                sessions.entrySet().removeIf(entry -> entry.getValue().equals(session))
        );
    }


    public void sendToUser(String group, Integer userId, String message) {
        Map<Integer, Session> sessions = groupSessions.get(group);
        if (sessions != null) {
            Session session = sessions.get(userId);
            if (session != null && session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void sendToAll(String group, String message) {
        Map<Integer, Session> sessions = groupSessions.get(group);
        if (sessions != null) {
            for (Session session : sessions.values()) {
                if (session != null && session.isOpen()) {
                    try {
                        session.getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}