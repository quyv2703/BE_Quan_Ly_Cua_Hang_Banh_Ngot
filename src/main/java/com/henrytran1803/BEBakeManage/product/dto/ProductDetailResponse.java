package com.henrytran1803.BEBakeManage.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailResponse {
    private Integer id;
    private String name;
    private String description;
    private Double currentPrice;
    private CategoryInfo category;
    private List<ProductBatchInfo> productBatches;
    private List<String> imageUrls;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryInfo {
        private Integer id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductBatchInfo {
        private Integer id;
        private LocalDateTime expirationDate;
        private Integer quantity;
        private String status;
        private Integer dailyDiscount;
    }
}