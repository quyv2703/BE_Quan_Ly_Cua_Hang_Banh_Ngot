package com.henrytran1803.BEBakeManage.recipe.dto;

import lombok.Data;
import java.util.List;

@Data
public class RecipeDTO {
    private int id;
    private String name;
    private List<RecipeDetailDTO> recipeDetails;

    @Data
    public static class RecipeDetailDTO {
        private int recipeId;
        private int ingredientId;
        private double quantity;
    }
}