package com.henrytran1803.BEBakeManage.export_ingredients.service;

import java.util.List;

import com.henrytran1803.BEBakeManage.export_ingredients.dto.ExportIngredientRequest;
import com.henrytran1803.BEBakeManage.export_ingredients.dto.ExportIngredientResponse;
import com.henrytran1803.BEBakeManage.export_ingredients.entity.ExportIngredient;

public interface ExportIngredientService {
    ExportIngredient exportIngredients(ExportIngredientRequest request);
    List<ExportIngredientResponse> getExportIngredients();
}