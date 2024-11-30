package com.henrytran1803.BEBakeManage.product_batches.service;

import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.product.dto.ProductSummaryDTO;
import com.henrytran1803.BEBakeManage.product_batches.dto.ProductBatchDetailDTO;

import java.util.List;

public interface ProductBatchService {
    ApiResponse<List<ProductSummaryDTO>> getListProductBatch();
    ApiResponse<List<ProductBatchDetailDTO>> getListProductBatchByStatues(List<String> statuses);

}
