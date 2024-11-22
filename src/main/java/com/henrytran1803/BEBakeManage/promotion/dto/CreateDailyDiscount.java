package com.henrytran1803.BEBakeManage.promotion.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class CreateDailyDiscount {
    @Min(value = 1, message = "Discount must be at least 1")
    @Max(value = 100, message = "Discount must be at most 100")
    private int discount;

    @NotEmpty(message = "Product IDs list cannot be empty")
    private List<@NotNull(message = "Product ID cannot be null") Integer> productBatchIds;

    @NotNull(message = "Skip default discount is required")
    private Boolean skipDefaultDiscount;

    @NotNull(message = "endDate is required")
    private LocalDateTime endDate;

    @NotNull(message = "getLastestDate is required")
    private Boolean getLastestDate;
}