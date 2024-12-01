package com.henrytran1803.BEBakeManage.nofication.controller;
import com.henrytran1803.BEBakeManage.nofication.service.NotificationService;
import com.henrytran1803.BEBakeManage.websocket.WebSocketSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.henrytran1803.BEBakeManage.nofication.model.NotificationMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;
@Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }



    @PostMapping("/send")
    public ResponseEntity<?> sendNotification() {
        try {
            notificationService.checkDatabaseTask();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to send notification: " + e.getMessage());
        }
    }
}