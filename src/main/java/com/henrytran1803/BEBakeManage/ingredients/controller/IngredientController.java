package com.henrytran1803.BEBakeManage.ingredients.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.ingredients.dto.IngredientCreationRequest;
import com.henrytran1803.BEBakeManage.ingredients.entity.Ingredients;
import com.henrytran1803.BEBakeManage.ingredients.service.IngredientService;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {
	@Autowired
    private IngredientService ingredientService;

	@GetMapping
    public ResponseEntity<ApiResponse<List<Ingredients>>> getActiveIngredients() {
        List<Ingredients> ingredients = ingredientService.getActiveIngredients();
        return ResponseEntity.ok(ApiResponse.success(ingredients));
    }
	
	@PostMapping
    public ResponseEntity<ApiResponse<Ingredients>> createIngredient(@RequestBody IngredientCreationRequest request) {
        Ingredients ingredient = ingredientService.createIngredient(request);
        return ResponseEntity.ok(ApiResponse.success(ingredient));
    }

	@PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Ingredients>> updateIngredient(@PathVariable int id, @RequestBody IngredientCreationRequest request) {
        Ingredients updatedIngredient = ingredientService.updateIngredient(id, request);
        return ResponseEntity.ok(ApiResponse.success(updatedIngredient));
    }

	@DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteIngredient(@PathVariable int id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
	
	@GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<Ingredients>>> searchIngredients(
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        Page<Ingredients> result = ingredientService.searchIngredients(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
