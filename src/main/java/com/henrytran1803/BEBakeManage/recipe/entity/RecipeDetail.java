package com.henrytran1803.BEBakeManage.recipe.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "recipe_details")
@Data
@NoArgsConstructor
public class RecipeDetail {
    @EmbeddedId
    private RecipeDetailId id;

    @ManyToOne
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id")
    @JsonBackReference
    private Recipe recipe;

    @Column(name = "quantity", nullable = false)
    private double quantity;

    public RecipeDetail(RecipeDetailId id, Recipe recipe, double quantity) {
        this.id = id;
        this.recipe = recipe;
        this.quantity = quantity;
    }
}

