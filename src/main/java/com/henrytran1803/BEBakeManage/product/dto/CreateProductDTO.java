package com.henrytran1803.BEBakeManage.product.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CreateProductDTO {
    private Integer categoryId;

    @NotBlank(message = "Name is required")
    @Size(max = 250, message = "Name must not exceed 250 characters")
    private String name;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Double price;

    @NotBlank(message = "Description is required")
    @Size(max = 250, message = "Description must not exceed 250 characters")
    private String description;


    @NotNull(message = "shelfLifeDays is required")
    @Min(value = 0, message = "shelfLifeDays must be greater than 0")
    private int shelfLifeDays;

    @NotNull(message = "Weight is required")
    @Min(value = 0, message = "Weight must be greater than 0")
    private Double weight;

    @NotNull(message = "Length is required")
    @Min(value = 0, message = "Length must be greater than 0")
    private Double length;

    @NotNull(message = "Width is required")
    @Min(value = 0, message = "Width must be greater than 0")
    private Double width;

    @NotNull(message = "Height is required")
    @Min(value = 0, message = "Height must be greater than 0")
    private Double height;

    @Min(value = 0, message = "Discount limit must be greater than or equal to 0")
    @Max(value = 100, message = "Discount limit must be less than or equal to 100")
    private Double discountLimit;

    @NotNull(message = "Recipe ID is required")
    private Integer recipeId;

    @NotNull(message = "Image URLs are required")
    private List<String> imageUrls; // Danh sách URL cho ảnh
}
