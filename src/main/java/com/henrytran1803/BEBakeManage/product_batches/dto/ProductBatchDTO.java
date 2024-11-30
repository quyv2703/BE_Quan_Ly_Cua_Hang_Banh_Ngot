package com.henrytran1803.BEBakeManage.product_batches.dto;

import lombok.Data;

@Data
public class ProductBatchDTO {
    private Integer id;
    private String expirationDate;
    private String status;
    private Integer quantity;

}
