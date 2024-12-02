package com.henrytran1803.BEBakeManage.PAYOS;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.henrytran1803.BEBakeManage.common.exception.error.QuyExeption;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.quycode.repository.BillDetailRepository;
import com.henrytran1803.BEBakeManage.quycode.repository.BillRepository;
import com.henrytran1803.BEBakeManage.quycode.response.BillResponse_View_Cake;
import com.henrytran1803.BEBakeManage.quycode.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.PaymentLinkData;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PayOSService {
    private final PayOS payOS;
    private final BillRepository billRepository;
    private final BillService billService;
    private final String BASE_URL = "http://127.0.0.1:3000";

    public ApiResponse<CheckoutResponseData> createPaymentLinkFromBill(Long billId) {
        try {
            ApiResponse<BillResponse_View_Cake> billResponse = billService.getBillDetailsById(billId);
            BillResponse_View_Cake bill = billResponse.getData();
            CheckoutResponseData paymentLink = createPaymentLink(
                    billId.toString(),
                    bill.getTotalAmount().intValue(),
                    "Đơn hàng #" + billId,
                    "Thanh toán đơn hàng #" + billId,
                    getSuccessUrl(billId),
                    getCancelUrl(billId)
            );
            return ApiResponse.Q_success(paymentLink, QuyExeption.SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.Q_failure(null,  QuyExeption.INVALID_INPUT);
        }
    }

    public CheckoutResponseData createPaymentLink(
            String orderId,
            int amount,
            String productName,
            String description,
            String returnUrl,
            String cancelUrl) throws Exception {

        ItemData item = ItemData.builder()
                .name(productName)
                .price(amount)
                .quantity(1)
                .build();

        PaymentData paymentData = PaymentData.builder()
                .orderCode(Long.parseLong(orderId))
                .description(description)
                .amount(amount)
                .item(item)
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl)
                .build();

        return payOS.createPaymentLink(paymentData);
    }

    public ApiResponse<PaymentLinkData> getPaymentInfo(Long orderId) {
        try {
            PaymentLinkData paymentInfo = payOS.getPaymentLinkInformation(orderId);
            return ApiResponse.Q_success(paymentInfo, QuyExeption.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.Q_failure(null, QuyExeption.INVALID_INPUT);
        }
    }

    public ApiResponse<PaymentLinkData> cancelPayment(Long orderId) {
        try {
            PaymentLinkData cancelledPayment = payOS.cancelPaymentLink(orderId, null);
            return ApiResponse.Q_success(cancelledPayment, QuyExeption.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.Q_failure(null, QuyExeption.INVALID_INPUT);
        }
    }

    public ApiResponse<String> confirmWebhook(Map<String, String> webhookData) {
        try {
            String result = payOS.confirmWebhook(webhookData.get("webhookUrl"));
            return ApiResponse.Q_success(result, QuyExeption.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.Q_failure(null, QuyExeption.INVALID_INPUT);
        }
    }

    private String getSuccessUrl(Long billId) {
        return BASE_URL + "/bills/" + billId + "/success";
    }

    private String getCancelUrl(Long billId) {
        return BASE_URL + "/bills/" + billId + "/cancel";
    }
}