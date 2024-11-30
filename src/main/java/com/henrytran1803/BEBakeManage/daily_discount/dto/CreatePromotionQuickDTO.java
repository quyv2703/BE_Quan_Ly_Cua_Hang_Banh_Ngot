package com.henrytran1803.BEBakeManage.daily_discount.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;
@Data
public class CreatePromotionQuickDTO {

    @Min(value = 1, message = "Discount must be at least 1")
    @Max(value = 100, message = "Discount must be at most 100")
    private int discount;

    @NotEmpty(message = "Product IDs list cannot be empty")
    private List<@NotNull(message = "Product ID cannot be null") Integer> productBatchIds;

    @NotNull(message = "Skip default discount is required")
    private Boolean skipDefaultDiscount;
}
