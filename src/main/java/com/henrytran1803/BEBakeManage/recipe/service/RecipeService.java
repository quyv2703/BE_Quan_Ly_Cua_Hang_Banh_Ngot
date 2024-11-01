package com.henrytran1803.BEBakeManage.recipe.service;

import com.henrytran1803.BEBakeManage.recipe.dto.CreateRecipeDTO;
import com.henrytran1803.BEBakeManage.recipe.dto.RecipeDTO;
import com.henrytran1803.BEBakeManage.recipe.entity.Recipe;

import java.util.Optional;


import com.henrytran1803.BEBakeManage.recipe.entity.RecipeDetail;
import java.util.List;

public interface RecipeService {
    List<Recipe> getAllRecipes();
    Optional<Recipe> getRecipeById(int id);
    Recipe createRecipe(CreateRecipeDTO createRecipeDTO);
    Recipe updateRecipe(Recipe updatedRecipe, List<RecipeDetail> updatedDetails);
    Optional<Recipe> deleteRecipeById(int id);
    Recipe updateRecipe(RecipeDTO recipeDTO) throws RuntimeException;

}
