package com.henrytran1803.BEBakeManage.import_ingredients.service;


import com.henrytran1803.BEBakeManage.import_ingredients.dto.ImportIngredientRequest;
import com.henrytran1803.BEBakeManage.import_ingredients.entity.ImportIngredient;

import jakarta.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public interface ImportIngredientService {
	ImportIngredient importIngredients(ImportIngredientRequest request);
    List<com.henrytran1803.BEBakeManage.import_ingredients.dto.ImportIngredientResponse> getImportIngredients();
}