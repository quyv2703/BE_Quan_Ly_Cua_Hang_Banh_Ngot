package com.henrytran1803.BEBakeManage.product_batches.controller;

import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.product.dto.ProductSummaryDTO;
import com.henrytran1803.BEBakeManage.product.service.ProductService;
import com.henrytran1803.BEBakeManage.product_batches.dto.ProductBatchDetailDTO;
import com.henrytran1803.BEBakeManage.product_batches.service.ProductBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/productbatches")
@RequiredArgsConstructor
public class ProductBatchController {
    private final ProductBatchService productBatchService;

    @GetMapping("")
    public ApiResponse<List<ProductSummaryDTO>> getListProductBatch() {
        return productBatchService.getListProductBatch();
    }
    @GetMapping("/statuses")
    public ApiResponse<List<ProductBatchDetailDTO>> getListProductBatchByStatus(@RequestParam List<String> statuses) {
        return productBatchService.getListProductBatchByStatues(statuses);
    }
}
