package com.henrytran1803.BEBakeManage.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductBatchDetailDTO {
    private Long id;
    private String name;
    private String status;
    private int currentDiscount;
    private int dailyDiscount;
    private int quantity;
    private LocalDate dateExpiry;
    private int countDown;
}
