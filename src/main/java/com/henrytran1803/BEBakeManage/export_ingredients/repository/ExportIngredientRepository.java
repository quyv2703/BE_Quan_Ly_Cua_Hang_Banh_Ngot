package com.henrytran1803.BEBakeManage.export_ingredients.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.henrytran1803.BEBakeManage.export_ingredients.entity.ExportIngredient;

public interface ExportIngredientRepository extends JpaRepository<ExportIngredient, Integer> {
	
}