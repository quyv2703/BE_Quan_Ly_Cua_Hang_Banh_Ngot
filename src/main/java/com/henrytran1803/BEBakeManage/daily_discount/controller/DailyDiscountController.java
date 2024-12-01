package com.henrytran1803.BEBakeManage.daily_discount.controller;

import com.henrytran1803.BEBakeManage.common.exception.error.DailyDiscountError;
import com.henrytran1803.BEBakeManage.common.exception.error.ErrorCode;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.daily_discount.dto.CreateDailyDiscount;
import com.henrytran1803.BEBakeManage.daily_discount.service.DailyDiscountService;
import com.henrytran1803.BEBakeManage.promotion.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/discounts")
public class DailyDiscountController {
    @Autowired
    private DailyDiscountService dailyDiscountService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<String>> createPromotionQuick(@RequestBody CreateDailyDiscount createDailyDiscount) {
        try {
            // Validate discount range
            if (createDailyDiscount.getDiscount() < 1 || createDailyDiscount.getDiscount() > 100) {
                return ResponseEntity.badRequest().body(ApiResponse.error(
                        DailyDiscountError.INVALID_DISCOUNT_RANGE.getCode(),
                        DailyDiscountError.INVALID_DISCOUNT_RANGE.getMessage()));
            }

            // Validate product batch ids
            if (createDailyDiscount.getProductBatchIds() == null || createDailyDiscount.getProductBatchIds().isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error(
                        DailyDiscountError.PRODUCT_BATCH_IDS_REQUIRED.getCode(),
                        DailyDiscountError.PRODUCT_BATCH_IDS_REQUIRED.getMessage()));
            }

            // Validate end date
            if (createDailyDiscount.getEndDate() == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error(
                        DailyDiscountError.END_DATE_REQUIRED.getCode(),
                        DailyDiscountError.END_DATE_REQUIRED.getMessage()));
            }

            // Validate skip default flag
            if (createDailyDiscount.getSkipDefaultDiscount() == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error(
                        DailyDiscountError.SKIP_DEFAULT_REQUIRED.getCode(),
                        DailyDiscountError.SKIP_DEFAULT_REQUIRED.getMessage()));
            }

            if (dailyDiscountService.createPromotionQuick(createDailyDiscount)) {
                return ResponseEntity.ok(ApiResponse.success(
                        DailyDiscountError.CREATE_DISCOUNT_SUCCESS.getMessage()));
            }

            return ResponseEntity.badRequest().body(ApiResponse.error(
                    DailyDiscountError.CREATE_DISCOUNT_FAILED.getCode(),
                    DailyDiscountError.CREATE_DISCOUNT_FAILED.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(
                    DailyDiscountError.CREATE_DISCOUNT_FAILED.getCode(),
                    e.getMessage()));
        }
    }
}