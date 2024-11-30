package com.henrytran1803.BEBakeManage.product_history.controller;

import com.henrytran1803.BEBakeManage.common.exception.error.ErrorCode;
import com.henrytran1803.BEBakeManage.common.exception.error.ProductPriceError;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.product.dto.ProductHistoryDTO;
import com.henrytran1803.BEBakeManage.product.service.ProductService;
import com.henrytran1803.BEBakeManage.product_history.service.ProductPriceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/price")
@RequiredArgsConstructor
public class ProductHistoryController {
    private final ProductPriceService productPriceService;

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updatePrice(
            @PathVariable("id") Integer productId,
            @RequestBody Map<String, Double> priceUpdate) {
        try {
            if (productId == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error(
                        ProductPriceError.PRODUCT_NOT_FOUND.getCode(),
                        ProductPriceError.PRODUCT_NOT_FOUND.getMessage()));
            }

            if (priceUpdate == null || !priceUpdate.containsKey("price")) {
                return ResponseEntity.badRequest().body(ApiResponse.error(
                        ProductPriceError.PRICE_INPUT_REQUIRED.getCode(),
                        ProductPriceError.PRICE_INPUT_REQUIRED.getMessage()));
            }

            Double price = priceUpdate.get("price");
            if (price == null || price < 1000) {
                return ResponseEntity.badRequest().body(ApiResponse.error(
                        ProductPriceError.PRICE_INVALID_RANGE.getCode(),
                        ProductPriceError.PRICE_INVALID_RANGE.getMessage()));
            }

            productPriceService.updateProductPrice(productId, price);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    ProductPriceError.UPDATE_PRICE_FAILED.getCode(),
                    e.getMessage()));
        }
    }

    @Transactional
    @GetMapping("/{id}/history")
    public ResponseEntity<ApiResponse<List<ProductHistoryDTO>>> getPriceHistory(
            @PathVariable("id") Integer productId) {
        try {
            if (productId == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error(
                        ProductPriceError.PRODUCT_NOT_FOUND.getCode(),
                        ProductPriceError.PRODUCT_NOT_FOUND.getMessage()));
            }

            List<ProductHistoryDTO> history = productPriceService.getAllProductHistoryByIdProduct(productId);
            return ResponseEntity.ok(ApiResponse.success(history));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    ProductPriceError.GET_HISTORY_FAILED.getCode(),
                    e.getMessage()));
        }
    }
}