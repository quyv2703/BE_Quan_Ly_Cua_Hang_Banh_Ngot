package com.henrytran1803.BEBakeManage.nofication.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.henrytran1803.BEBakeManage.ingredients.repository.IngredientRepository;
import com.henrytran1803.BEBakeManage.nofication.model.NotificationMessage;
import com.henrytran1803.BEBakeManage.product_batches.service.ProductBatchService;
import com.henrytran1803.BEBakeManage.websocket.WebSocketSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTaskService {

    private final ProductBatchService productBatchService;
    private final WebSocketSessionManager sessionManager;
    private final ObjectMapper objectMapper;

    @Autowired
    public ScheduledTaskService(
            ProductBatchService productBatchService,
            WebSocketSessionManager sessionManager,
            ObjectMapper objectMapper
    ) {
        this.productBatchService = productBatchService;
        this.sessionManager = sessionManager;
        this.objectMapper = objectMapper;
    }

    @Scheduled(cron = "1 * * * * *")
    public void checkDatabaseTask() {
        try {
            long nearExpiryCount = productBatchService.countByStatus("NEAR_EXPIRY");
            long expiredCount = productBatchService.countByStatus("EXPIRED");
            System.out.println(nearExpiryCount);
            System.out.println(expiredCount);

            if (nearExpiryCount > 0 || expiredCount > 0) {
                NotificationMessage notification = new NotificationMessage();
                notification.setType("PRODUCT_EXPIRY_ALERT");
                notification.setMessage(String.format(
                        "Cảnh báo: Có %d sản phẩm sắp hết hạn và %d sản phẩm đã hết hạn.",
                        nearExpiryCount, expiredCount
                ));
                notification.setSeverity(NotificationMessage.MessageSeverity.WARNING);
                notification.setDuration(5000);
                String jsonMessage = objectMapper.writeValueAsString(notification);
                sessionManager.sendToAll("authenticated", jsonMessage);
            }
        } catch (Exception e) {
            try {
                NotificationMessage errorNotification = new NotificationMessage();
                errorNotification.setType("SYSTEM_ERROR");
                errorNotification.setMessage("Lỗi hệ thống khi kiểm tra sản phẩm");
                errorNotification.setSeverity(NotificationMessage.MessageSeverity.ERROR);
                errorNotification.setDuration(3000);
                String jsonError = objectMapper.writeValueAsString(errorNotification);
                sessionManager.sendToAll("authenticated", jsonError);
            } catch (Exception ex) {
                System.err.println("Error sending error notification: " + ex.getMessage());
            }
        }
    }
}
