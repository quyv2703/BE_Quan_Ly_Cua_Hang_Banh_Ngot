package com.henrytran1803.BEBakeManage.recipe.dto;

import lombok.Data;

@Data
public class GetRecipeDetailDTO {
    private int ingredientId;
    private String ingredientName;
    private double quantity;
}
