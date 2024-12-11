package com.henrytran1803.BEBakeManage.disposed_product.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DisposedBatchDTO {
    private int batchId;
    private String productName;
    private int disposedQuantity;
    private LocalDateTime manufacturingDate;
}