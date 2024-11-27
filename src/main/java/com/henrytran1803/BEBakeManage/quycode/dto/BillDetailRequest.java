package com.henrytran1803.BEBakeManage.quycode.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillDetailRequest {
    private Long productBatchId;
    private Integer quantity;
    private Double price;

    // Getters and Setters
}
