package com.henrytran1803.BEBakeManage.PAYOS;
import java.util.Date;
import java.util.Map;

import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.PaymentLinkData;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PayOSController {
    private final PayOSService payOSService;

    @GetMapping("/bill/{billId}")
    public ResponseEntity<ApiResponse<CheckoutResponseData>> createPaymentForBill(@PathVariable Long billId) {
        return ResponseEntity.ok(payOSService.createPaymentLinkFromBill(billId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<PaymentLinkData>> getPaymentInfo(@PathVariable Long orderId) {
        return ResponseEntity.ok(payOSService.getPaymentInfo(orderId));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<PaymentLinkData>> cancelPayment(@PathVariable Long orderId) {
        return ResponseEntity.ok(payOSService.cancelPayment(orderId));
    }

//    @PostMapping("/webhook")
//    public ResponseEntity<ApiResponse<String>> handleWebhook(@RequestBody Map<String, String> webhookData) {
//        return ResponseEntity.ok(payOSService.confirmWebhook(webhookData));
//    }
}