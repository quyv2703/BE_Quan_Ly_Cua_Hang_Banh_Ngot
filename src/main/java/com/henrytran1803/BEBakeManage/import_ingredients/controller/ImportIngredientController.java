package com.henrytran1803.BEBakeManage.import_ingredients.controller;

import com.henrytran1803.BEBakeManage.common.exception.error.ErrorCode;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.import_ingredients.dto.ImportIngredientRequest;
import com.henrytran1803.BEBakeManage.import_ingredients.dto.ImportIngredientResponse;
import com.henrytran1803.BEBakeManage.import_ingredients.entity.ImportIngredient;
import com.henrytran1803.BEBakeManage.import_ingredients.service.ImportIngredientService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ingredients/import")
public class ImportIngredientController {

    @Autowired
    private ImportIngredientService importIngredientService;


    @PostMapping
    public ResponseEntity<ApiResponse<ImportIngredient>> importIngredients(@Valid @RequestBody ImportIngredientRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.IMPORT_INGREDIENT_CREATE_FAIL.name(),  bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }

        try {
            ImportIngredient importedIngredient = importIngredientService.importIngredients(request);
            return ResponseEntity.ok(ApiResponse.success(importedIngredient));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.IMPORT_INGREDIENT_CREATE_FAIL.name(), e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ImportIngredientResponse>>> getAllImportIngredients() {
        try {
            List<ImportIngredientResponse> importIngredients = importIngredientService.getImportIngredients();
            return ResponseEntity.ok(ApiResponse.success(importIngredients));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.IMPORT_INGREDIENT_FETCH_FAIL.name(), e.getMessage()));
        }
    }
}