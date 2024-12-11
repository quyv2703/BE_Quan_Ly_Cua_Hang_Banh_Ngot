package com.henrytran1803.BEBakeManage.disposed_product.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DisposedProductSummaryDTO {
    private int id;
    private LocalDateTime dateDisposed;
    private int totalBatches;
    private int totalQuantityDisposed;
    private String note;
    private String staffName;
}