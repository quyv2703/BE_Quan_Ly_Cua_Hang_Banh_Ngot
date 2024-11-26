package com.henrytran1803.BEBakeManage.ingredients.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.henrytran1803.BEBakeManage.ingredients.dto.IngredientCreationRequest;
import com.henrytran1803.BEBakeManage.ingredients.entity.Ingredients;

public interface IngredientService {
    List<Ingredients> getActiveIngredients();
    List<Ingredients> getAllIngredients();
    Ingredients createIngredient(IngredientCreationRequest request);

    Ingredients updateIngredient(int id, IngredientCreationRequest request);

    void deleteIngredient(int id);

    Page<Ingredients> searchIngredients(String keyword, Pageable pageable);
}
