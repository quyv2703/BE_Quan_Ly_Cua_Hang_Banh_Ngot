package com.henrytran1803.BEBakeManage.product.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private Integer id;
    private String name;
    private Double currentPrice;
    private String categoryName;
    private  Boolean status;
}
