package com.henrytran1803.BEBakeManage.product.dto;

import com.henrytran1803.BEBakeManage.product_history.entity.ProductHistory;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductHistoryDTO {
    private Integer id;
    private Double price;
    private LocalDateTime effectiveDate;

    public ProductHistoryDTO(ProductHistory productHistory) {
        this.id = productHistory.getId();
        this.price = productHistory.getPrice();
        this.effectiveDate = productHistory.getEffectiveDate();
    }
}

