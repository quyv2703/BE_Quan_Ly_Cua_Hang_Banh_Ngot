package com.henrytran1803.BEBakeManage.product.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {
    private Integer id;
    private String name;
    private Double currentPrice;
    private String categoryName;
    private  Boolean status;
    private List<String> imageUrls; // Danh sách URL của ảnh

}
