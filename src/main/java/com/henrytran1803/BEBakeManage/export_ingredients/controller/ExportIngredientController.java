package com.henrytran1803.BEBakeManage.export_ingredients.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.henrytran1803.BEBakeManage.common.exception.error.ErrorCode;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.export_ingredients.dto.ExportIngredientRequest;
import com.henrytran1803.BEBakeManage.export_ingredients.dto.ExportIngredientResponse;
import com.henrytran1803.BEBakeManage.export_ingredients.entity.ExportIngredient;
import com.henrytran1803.BEBakeManage.export_ingredients.service.ExportIngredientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ingredients/export")
public class ExportIngredientController {

    @Autowired
    private ExportIngredientService exportIngredientService;

    @PostMapping
    public ResponseEntity<ApiResponse<ExportIngredient>> exportIngredients(@Valid @RequestBody ExportIngredientRequest request, BindingResult bindingResult) {
        // Kiểm tra lỗi dữ liệu đầu vào
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.EXPORT_INGREDIENT_CREATE_FAIL.name(), bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }

        try {
            ExportIngredient exportedIngredient = exportIngredientService.exportIngredients(request);
            return ResponseEntity.ok(ApiResponse.success(exportedIngredient));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.EXPORT_INGREDIENT_CREATE_FAIL.name(), e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExportIngredientResponse>>> getAllExportIngredients() {
        try {
            List<ExportIngredientResponse> exportIngredients = exportIngredientService.getExportIngredients();
            return ResponseEntity.ok(ApiResponse.success(exportIngredients));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.EXPORT_INGREDIENT_FETCH_FAIL.name(), e.getMessage()));
        }
    }
}