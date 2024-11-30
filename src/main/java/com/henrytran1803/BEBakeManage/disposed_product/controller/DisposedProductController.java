package com.henrytran1803.BEBakeManage.disposed_product.controller;

import com.henrytran1803.BEBakeManage.common.exception.error.DisposedProductError;
import com.henrytran1803.BEBakeManage.common.exception.error.ErrorCode;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.disposed_product.dto.DisposedProductDTO;
import com.henrytran1803.BEBakeManage.disposed_product.service.DisposedProductService;
import com.henrytran1803.BEBakeManage.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/disposed")
@RequiredArgsConstructor
public class DisposedProductController {
    private final DisposedProductService disposedProduct;

    @PostMapping("")
    public ResponseEntity<ApiResponse<String>> getListProductBatchByStatus(@RequestBody DisposedProductDTO disposedProductDTO) {
        if (disposedProductDTO.getProductBatchIds() == null || disposedProductDTO.getProductBatchIds().isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    DisposedProductError.BATCH_IDS_REQUIRED.getCode(),
                    DisposedProductError.BATCH_IDS_REQUIRED.getMessage()));
        }

        try {
            if (disposedProduct.disposedProduct(disposedProductDTO)) {
                return ResponseEntity.ok().body(ApiResponse.success(DisposedProductError.DISPOSED_SUCCESS.getMessage()));
            }
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    DisposedProductError.DISPOSED_FAILED.getCode(),
                    DisposedProductError.DISPOSED_FAILED.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    DisposedProductError.DISPOSED_FAILED.getCode(),
                    e.getMessage()));
        }
    }
}
