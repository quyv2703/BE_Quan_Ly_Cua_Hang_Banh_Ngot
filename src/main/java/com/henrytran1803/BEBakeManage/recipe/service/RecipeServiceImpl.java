package com.henrytran1803.BEBakeManage.recipe.service;

import com.henrytran1803.BEBakeManage.recipe.dto.CreateRecipeDTO;
import com.henrytran1803.BEBakeManage.recipe.dto.RecipeDTO;
import com.henrytran1803.BEBakeManage.recipe.entity.Recipe;
import com.henrytran1803.BEBakeManage.recipe.entity.RecipeDetail;
import com.henrytran1803.BEBakeManage.recipe.entity.RecipeDetailId;
import com.henrytran1803.BEBakeManage.recipe.repository.RecipeRepository;
import com.henrytran1803.BEBakeManage.recipe.repository.RecipeDetailRepository;
import com.henrytran1803.BEBakeManage.product.repository.ProductRepository;
import com.henrytran1803.BEBakeManage.common.exception.error.ErrorCode;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeDetailRepository recipeDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    @Override
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Transactional
    @Override
    public Optional<Recipe> getRecipeById(int id) {
        return recipeRepository.findById(id);
    }

    @Override
    public Recipe createRecipe(CreateRecipeDTO createRecipeDTO) {
        if (createRecipeDTO.getName() == null  || createRecipeDTO.getName().trim().isEmpty()) {
            throw new RuntimeException("Recipe name cannot be null or empty");
        }

        if (recipeRepository.existsByName(createRecipeDTO.getName())) {
            throw new RuntimeException("Recipe with this name already exists");
        }

        Recipe recipe = new Recipe(createRecipeDTO.getName());
        Recipe savedRecipe = recipeRepository.save(recipe);

        for (CreateRecipeDTO.RecipeDetailDTO detailDTO : createRecipeDTO.getRecipeDetails()) {
            RecipeDetailId recipeDetailId = new RecipeDetailId(savedRecipe.getId(), detailDTO.getIngredientId());
            RecipeDetail detail = new RecipeDetail(recipeDetailId, savedRecipe, detailDTO.getQuantity());
            recipeDetailRepository.save(detail);
        }

        return savedRecipe;
    }

    @Override
    public Recipe updateRecipe( Recipe updatedRecipe, List<RecipeDetail> updatedDetails) {
        Recipe recipe = recipeRepository.findById(updatedRecipe.getId())
                .orElseThrow();
        recipe.setName(updatedRecipe.getName());
        recipeDetailRepository.deleteByRecipeId(updatedRecipe.getId());
        for (RecipeDetail detail : updatedDetails) {
            detail.setRecipe(recipe);
            recipeDetailRepository.save(detail);
        }
        return recipeRepository.save(recipe);
    }
    @Override
    public Optional<Recipe> deleteRecipeById(int id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isPresent()) {
            recipeRepository.deleteById(id);
        }
        return recipe;
    }

    @Override
    @Transactional
    public Recipe updateRecipe(RecipeDTO recipeDTO) {
        Recipe existingRecipe = recipeRepository.findById(recipeDTO.getId())
                .orElseThrow(() -> new RuntimeException("Recipe not found with id: " + recipeDTO.getId()));
        if (recipeDTO.getName() == null || recipeDTO.getName().trim().isEmpty()) {
            throw new RuntimeException("Recipe name cannot be null or empty");
        }
        Recipe recipeWithSameName = recipeRepository.findByName(recipeDTO.getName());
        if (recipeWithSameName != null && recipeWithSameName.getId() != recipeDTO.getId()) {
            throw new RuntimeException("Recipe with name '" + recipeDTO.getName() + "' already exists");
        }
        existingRecipe.setName(recipeDTO.getName());
        existingRecipe.clearRecipeDetails();
        if (recipeDTO.getRecipeDetails() != null) {
            for (RecipeDTO.RecipeDetailDTO detailDTO : recipeDTO.getRecipeDetails()) {
                if (detailDTO.getQuantity() <= 0) {
                    throw new RuntimeException("Quantity must be greater than 0 for ingredient: " + detailDTO.getIngredientId());
                }
                RecipeDetailId detailId = new RecipeDetailId(existingRecipe.getId(), detailDTO.getIngredientId());
                RecipeDetail detail = new RecipeDetail(detailId, existingRecipe, detailDTO.getQuantity());
                existingRecipe.addRecipeDetail(detail);
            }
        }
        return recipeRepository.save(existingRecipe);
    }

    @Transactional
    @Override
    public RecipeDTO findRecipeByProductId(int productId) {
        Recipe recipe = productRepository.findRecipeByProductId(productId);

        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setId(recipe.getId());
        recipeDTO.setName(recipe.getName());

        List<RecipeDTO.RecipeDetailDTO> detailDTOs = new ArrayList<>();
        for (RecipeDetail detail : recipe.getRecipeDetails()) {
            RecipeDTO.RecipeDetailDTO detailDTO = new RecipeDTO.RecipeDetailDTO();
            detailDTO.setRecipeId(detail.getId().getRecipeId());
            detailDTO.setIngredientId(detail.getId().getIngredientId());
            detailDTO.setQuantity(detail.getQuantity());
            detailDTOs.add(detailDTO);
        }

        recipeDTO.setRecipeDetails(detailDTOs);
        return recipeDTO;
    }


}
