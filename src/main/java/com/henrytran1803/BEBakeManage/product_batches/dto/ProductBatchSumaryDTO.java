package com.henrytran1803.BEBakeManage.product_batches.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class ProductBatchSumaryDTO {
    private Long id;
    private String status;
    private int quantity;
    private int dailyDiscount;
    private LocalDate dateExpiry;
    private int countDown;
}
