package com.henrytran1803.BEBakeManage.product.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class ProductBatchSumaryDTO {
    private Long id;
    private String status;
    private int quantity;
    private LocalDate dateExpiry; // Hoặc LocalDate nếu dùng định dạng ngày
    private int countDown;
}
