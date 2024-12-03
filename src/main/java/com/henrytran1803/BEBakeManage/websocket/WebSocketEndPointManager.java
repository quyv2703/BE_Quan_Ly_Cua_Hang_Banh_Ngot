package com.henrytran1803.BEBakeManage.websocket;

import com.henrytran1803.BEBakeManage.common.util.JwtUtils;
import com.henrytran1803.BEBakeManage.config.WebSocketConfig;
import io.jsonwebtoken.Claims;
import jakarta.websocket.*;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
@Component
@ServerEndpoint(value = "/websocket", configurator = WebSocketConfig.class)
public class WebSocketEndPointManager {
    private static WebSocketSessionManager sessionManager;
    private static JwtUtils jwtUtils;

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        WebSocketEndPointManager.jwtUtils = jwtUtils;
    }

    @Autowired
    public void setWebSocketSessionManager(WebSocketSessionManager manager) {
        WebSocketEndPointManager.sessionManager = manager;
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        try {
            HandshakeRequest request = (HandshakeRequest) config.getUserProperties().get("handshakerequest");
            String token = extractTokenFromHeaderOrQuery(request);

            if (token != null) {
                Claims claims = jwtUtils.parseJwtToken(token);
                Integer userId = claims.get("userId", Integer.class);

                if (userId != null) {
                    sessionManager.addSession("authenticated", userId, session);
                    System.out.println("New session opened for user " + userId + ": " + session.getId());
                } else {
                    closeSession(session, "Invalid user ID in token");
                }
            } else {
                closeSession(session, "No authorization token provided");
            }
        } catch (Exception e) {
            e.printStackTrace();
            closeSession(session, "Error processing token");
        }
    }
    private String extractTokenFromHeaderOrQuery(HandshakeRequest request) {
        String token = null;

        // Extract from headers
        List<String> authorizationHeaders = request.getHeaders().get("Authorization");
        if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
            token = authorizationHeaders.get(0).replace("Bearer ", "");
        }

        // Extract from query string
        if (token == null) {
            String queryString = request.getQueryString();
            if (queryString != null) {
                token = extractTokenFromQuery(queryString);
            }
        }

        return token;
    }

    private String extractTokenFromQuery(String queryString) {
        for (String param : queryString.split("&")) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2 && "token".equals(keyValue[0])) {
                return keyValue[1];
            }
        }
        return null;
    }

    private void closeSession(Session session, String reason) {
        try {
            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, reason));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
