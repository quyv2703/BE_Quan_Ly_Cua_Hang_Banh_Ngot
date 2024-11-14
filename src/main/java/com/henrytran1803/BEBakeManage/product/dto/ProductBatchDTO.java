package com.henrytran1803.BEBakeManage.product.dto;

import lombok.Data;

@Data
public class ProductBatchDTO {
    private Integer id;
    private String expirationDate;
    private Double currentDiscount;
    private String status;
    private Integer quantity;

}
