package com.henrytran1803.BEBakeManage.ingredients.service;

import com.henrytran1803.BEBakeManage.ingredients.dto.IngredientCreationRequest;
import com.henrytran1803.BEBakeManage.ingredients.entity.Ingredients;
import com.henrytran1803.BEBakeManage.ingredients.repository.IngredientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class IngredientServiceImpl implements IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    public List<Ingredients> getActiveIngredients() {
        return ingredientRepository.findByIsactiveTrue();
    }

    @Override
    public List<Ingredients> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    @Override
    public Ingredients createIngredient(IngredientCreationRequest request) {
        Ingredients ingredient = new Ingredients();
        ingredient.setName(request.getName());
        ingredient.setUnit_id(request.getUnit_id());
        ingredient.setWarning_limits(request.getWarning_limits());
        ingredient.setQuantity(0); // Default quantity = 0
        ingredient.setIsactive(true); // Default active = true

        return ingredientRepository.save(ingredient);
    }

    @Override
    public Ingredients updateIngredient(int id, IngredientCreationRequest request) {
        Optional<Ingredients> optionalIngredient = ingredientRepository.findById(id);

        if (optionalIngredient.isPresent()) {
            Ingredients ingredient = optionalIngredient.get();
            ingredient.setName(request.getName());
            ingredient.setUnit_id(request.getUnit_id());
            ingredient.setWarning_limits(request.getWarning_limits());

            return ingredientRepository.save(ingredient);
        } else {
            throw new RuntimeException("Ingredient not found with id " + id);
        }
    }

    @Override
    public void deleteIngredient(int id) {
        Optional<Ingredients> optionalIngredient = ingredientRepository.findById(id);

        if (optionalIngredient.isPresent()) {
            Ingredients ingredient = optionalIngredient.get();
            ingredient.setIsactive(false);
            ingredientRepository.save(ingredient);
        } else {
            throw new RuntimeException("Ingredient not found with id " + id);
        }
    }

    @Override
    public Page<Ingredients> searchIngredients(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return ingredientRepository.findAll(pageable);
        }
        return ingredientRepository.searchByKeyword(keyword, pageable);
    }
}