package com.henrytran1803.BEBakeManage.recipe.dto;

import com.henrytran1803.BEBakeManage.recipe.entity.Recipe;
import com.henrytran1803.BEBakeManage.recipe.entity.RecipeDetail;
import lombok.Data;

import java.util.List;

@Data
public class CreateRecipeDTO {
    private String name;
    private List<RecipeDetailDTO> recipeDetails;

    @Data
    public static class RecipeDetailDTO {
        private int ingredientId;
        private double quantity;
    }
}
