package com.henrytran1803.BEBakeManage.product.dto;

import com.henrytran1803.BEBakeManage.product_batches.dto.ProductBatchSumaryDTO;
import lombok.Data;

import java.util.List;
@Data
public class ProductSummaryDTO {
    private Long id;
    private String name;
    private int shelfLifeDays;
    private int totalProductBatch;
    private int totalNearExpiry;
    private int totalExpiry;
    private List<ProductBatchSumaryDTO> productBatches;
}
