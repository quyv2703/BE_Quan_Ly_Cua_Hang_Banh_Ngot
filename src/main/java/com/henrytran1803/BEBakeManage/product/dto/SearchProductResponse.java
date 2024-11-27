package com.henrytran1803.BEBakeManage.product.dto;


import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchProductResponse {
    private Integer id;
    private String name;
    private String imageUrl;
    private String categoryName;
    private Integer categoryId;
    private Integer maxDailyDiscount;
    private Double price;
    private Integer totalQuantity;
}