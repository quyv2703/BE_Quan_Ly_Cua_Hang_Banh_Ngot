package com.henrytran1803.BEBakeManage.import_ingredients.repository;

import com.henrytran1803.BEBakeManage.import_ingredients.entity.ImportIngredient;
import com.henrytran1803.BEBakeManage.import_ingredients.entity.ImportIngredientDetailId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportIngredientRepository extends JpaRepository<ImportIngredient, ImportIngredientDetailId> {
	
}