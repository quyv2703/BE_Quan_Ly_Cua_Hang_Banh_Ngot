package com.henrytran1803.BEBakeManage.export_ingredients.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.henrytran1803.BEBakeManage.export_ingredients.entity.ExportIngredientDetail;
import com.henrytran1803.BEBakeManage.export_ingredients.entity.ExportIngredientDetailId;

public interface ExportIngredientDetailRepository extends JpaRepository<ExportIngredientDetail, ExportIngredientDetailId> {

}