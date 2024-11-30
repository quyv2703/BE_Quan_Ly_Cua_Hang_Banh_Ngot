package com.henrytran1803.BEBakeManage.disposed_product.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
@Data
public class DisposedProductDTO {
    private String note;
    @NotNull(message = "productBatchIds is required")
    private List<Integer> productBatchIds;
}
