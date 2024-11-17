package com.henrytran1803.BEBakeManage.Image.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateImageDTO {
    @NotNull(message = "Product ID is required")
    private Integer productId;

    @NotBlank(message = "URL is required")
    private String url;
}
