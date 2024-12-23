package com.henrytran1803.BEBakeManage.recipe.controller;

import com.henrytran1803.BEBakeManage.category.entity.Category;
import com.henrytran1803.BEBakeManage.common.exception.error.ErrorCode;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.recipe.dto.CreateRecipeDTO;
import com.henrytran1803.BEBakeManage.recipe.dto.GetRecipeDTO;
import com.henrytran1803.BEBakeManage.recipe.dto.RecipeDTO;
import com.henrytran1803.BEBakeManage.recipe.entity.Recipe;
import com.henrytran1803.BEBakeManage.recipe.entity.RecipeDetail;
import com.henrytran1803.BEBakeManage.recipe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Recipe>>> getAllRecipes() {
        return ResponseEntity.ok(ApiResponse.success(recipeService.getAllRecipes()));
    }
    @PostMapping
    public ResponseEntity<ApiResponse<Recipe>> createRecipe(@RequestBody CreateRecipeDTO createRecipeDTO) {
        try {
            Recipe createdRecipe = recipeService.createRecipe(createRecipeDTO);
            return ResponseEntity.ok(ApiResponse.success(createdRecipe));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.RECIPE_CREATE_FAILED.getCode(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.RECIPE_CREATE_FAILED.getCode(), "Failed to create recipe due to unexpected error"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetRecipeDTO>> getRecipeById(@PathVariable("id") int id) {
        try {
            GetRecipeDTO recipeOptional = recipeService.getRecipeById(id);
            return ResponseEntity.ok(ApiResponse.success(recipeOptional));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.RECIPE_CREATE_FAILED.getCode(), "Failed to retrieve recipe"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.RECIPE_CREATE_FAILED.getCode(), "Failed to retrieve recipe"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteRecipeById(@PathVariable int id) {
        try {
            Optional<Recipe> deletedRecipe = recipeService.deleteRecipeById(id);
            if (deletedRecipe.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("Recipe deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(ErrorCode.RECIPE_NOT_FOUND.name(), "Recipe not found"));
            }
        } catch (IllegalStateException e) {
            // Xử lý lỗi recipe đang được sử dụng trong product
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(ErrorCode.RECIPE_IN_USE.name(), e.getMessage()));
        } catch (Exception e) {
            // Xử lý các lỗi khác
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(ErrorCode.RECIPE_DELETE_FAILED.name(), "Failed to delete recipe: " + e.getMessage()));
        }
    }
    @PutMapping
    public ResponseEntity<ApiResponse<Recipe>> updateRecipe(@RequestBody RecipeDTO recipeDTO) {
        try {
            Recipe updatedRecipe = recipeService.updateRecipe(recipeDTO);
            return ResponseEntity.ok(ApiResponse.success(updatedRecipe));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(ErrorCode.RECIPE_UPDATE_FAILED.name(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR.name(), "An unexpected error occurred"));
        }
    }

    @GetMapping("/findByProduct/{id}")
    public ResponseEntity<ApiResponse<RecipeDTO>> findRecipeByProductId(@PathVariable int id) {
        RecipeDTO recipeDTO = recipeService.findRecipeByProductId(id);
        return ResponseEntity.ok(ApiResponse.success(recipeDTO));
    }

}
