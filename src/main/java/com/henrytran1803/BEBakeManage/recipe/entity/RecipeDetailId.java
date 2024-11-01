package com.henrytran1803.BEBakeManage.recipe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class RecipeDetailId implements java.io.Serializable {
    @Column(name = "recipe_id")
    private int recipeId;

    @Column(name = "ingredient_id")
    private int ingredientId;

    public RecipeDetailId(int recipeId, int ingredientId) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
    }
}