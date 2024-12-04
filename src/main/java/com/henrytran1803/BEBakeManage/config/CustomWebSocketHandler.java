//package com.henrytran1803.BEBakeManage.config;
//
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.WebSocketMessage;
//import org.springframework.web.socket.WebSocketSession;
//
//class CustomWebSocketHandler implements WebSocketHandler {
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        // Xử lý khi kết nối được thiết lập
//        System.out.println("Connection established: " + session.getId());
//    }
//
//    @Override
//    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
//        // Xử lý khi nhận được tin nhắn
//        String payload = message.getPayload().toString();
//        System.out.println("Received message: " + payload);
//    }
//
//    @Override
//    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
//        // Xử lý khi có lỗi
//        System.out.println("Error occurred: " + exception.getMessage());
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
//        // Xử lý khi kết nối đóng
//        System.out.println("Connection closed: " + session.getId());
//    }
//
//    @Override
//    public boolean supportsPartialMessages() {
//        return false;
//    }
//}