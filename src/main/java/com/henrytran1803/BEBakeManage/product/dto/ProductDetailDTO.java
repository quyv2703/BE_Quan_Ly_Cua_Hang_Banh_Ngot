package com.henrytran1803.BEBakeManage.product.dto;


import lombok.Data;

import java.util.List;

@Data
public class ProductDetailDTO {
    private Integer id;
    private Integer categoryId;
    private String name;
    private Double currentPrice;
    private String description;
    private int shelfLifeDays;
    private int shelfLifeDaysWarning;
    private Boolean status;
    private Double weight;
    private Double length;
    private Double width;
    private Double height;
    private Double discountLimit;
    private Integer recipeId;
    private CategoryInfo category;
    private RecipeInfo recipe;
    private List<ImageInfo> images;
    @Data
    public static class CategoryInfo {
        private Integer id;
        private String name;
    }
    @Data
    public static class RecipeInfo {
        private Integer id;
        private String name;
    }
    @Data
    public static class ImageInfo {
        private Integer id;
        private String url;
    }
}