package com.henrytran1803.BEBakeManage.quycode.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BillDetailDTO {
    private Long id;
    private Long productBatchId;
    private Integer quantity;
    private Double price;

    // Constructor, getters, and setters
}
