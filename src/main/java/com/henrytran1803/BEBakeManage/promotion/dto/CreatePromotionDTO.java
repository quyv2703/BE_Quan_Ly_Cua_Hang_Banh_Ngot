package com.henrytran1803.BEBakeManage.promotion.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@Data
public class CreatePromotionDTO {
    @NotNull(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    @NotNull(message = "Description is required")
    @Size(min = 1, max = 250, message = "Description must be between 1 and 250 characters")
    private String description;
    @NotNull(message = "Discount is required")
    @Size(min = 1, max = 100, message = "Discount must be between 1 and 100")
    private int discount;
    @NotNull(message = "Start date is required")
    @Future(message = "Start date must be in the future")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;

    @NotEmpty(message = "Product IDs list cannot be empty")
    private List<@NotNull(message = "Product ID cannot be null") Integer> productIds;
}
