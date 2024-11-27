package com.henrytran1803.BEBakeManage.recipe.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "recipes")
@Data
@NoArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RecipeDetail> recipeDetails = new ArrayList<>();

    public Recipe(String name) {
        this.name = name;
    }

    // Helper method để quản lý relationship
    public void addRecipeDetail(RecipeDetail detail) {
        recipeDetails.add(detail);
        detail.setRecipe(this);
    }

    public void removeRecipeDetail(RecipeDetail detail) {
        recipeDetails.remove(detail);
        detail.setRecipe(null);
    }

    public void clearRecipeDetails() {
        for (RecipeDetail detail : new ArrayList<>(recipeDetails)) {
            removeRecipeDetail(detail);
        }
    }
}
