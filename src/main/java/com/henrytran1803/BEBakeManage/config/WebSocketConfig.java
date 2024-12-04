package com.henrytran1803.BEBakeManage.config;

import com.henrytran1803.BEBakeManage.common.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.ServerEndpointConfig;

import java.util.Map;
@Configuration
@EnableWebSocket
public class WebSocketConfig extends ServerEndpointConfig.Configurator implements WebSocketConfigurer {

    @Autowired
    private JwtUtils jwtUtils;

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public CustomWebSocketHandler webSocketHandler() {
        return new CustomWebSocketHandler();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/websocket")
                .addInterceptors(handshakeInterceptor())
                .setAllowedOrigins("*");
    }

    @Bean
    public HandshakeInterceptor handshakeInterceptor() {
        return new HandshakeInterceptor() {
            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                           WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                if (request instanceof ServletServerHttpRequest) {
                    ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;

                    // Lấy token từ URL parameter
                    String token = extractTokenFromUrl(servletRequest);

                    if (token != null) {
                        try {
                            Claims claims = jwtUtils.parseJwtToken(token);
                            Integer userId = claims.get("userId", Integer.class);
                            attributes.put("userId", userId);
                            return true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }
                return false;
            }

            private String extractTokenFromUrl(ServletServerHttpRequest request) {
                String query = request.getServletRequest().getQueryString();
                if (query != null) {
                    String[] params = query.split("&");
                    for (String param : params) {
                        String[] keyValue = param.split("=");
                        if (keyValue.length == 2 && "token".equals(keyValue[0])) {
                            return keyValue[1];
                        }
                    }
                }
                return null;
            }

            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                       WebSocketHandler wsHandler, Exception exception) {
                // Nothing to do here
            }
        };
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig sec,
                                jakarta.websocket.server.HandshakeRequest request,
                                jakarta.websocket.HandshakeResponse response) {
        sec.getUserProperties().put("handshakerequest", request);
    }
}