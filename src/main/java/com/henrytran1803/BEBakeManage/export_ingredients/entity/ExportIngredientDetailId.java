package com.henrytran1803.BEBakeManage.export_ingredients.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ExportIngredientDetailId implements Serializable {
	//Thêm serialVersionUID
    private static final long serialVersionUID = 1L;
	
	private int export_ingredient_id;
    private int ingredient_id;

    public ExportIngredientDetailId() {}

    public ExportIngredientDetailId(int export_ingredient_id, int ingredient_id) {
        this.export_ingredient_id = export_ingredient_id;
        this.ingredient_id = ingredient_id;
    }
    
    public int getExport_ingredient_id() {
		return export_ingredient_id;
	}

	public void setExport_ingredient_id(int export_ingredient_id) {
		this.export_ingredient_id = export_ingredient_id;
	}

	public int getIngredient_id() {
		return ingredient_id;
	}

	public void setIngredient_id(int ingredient_id) {
		this.ingredient_id = ingredient_id;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExportIngredientDetailId that = (ExportIngredientDetailId) o;
        return export_ingredient_id == that.export_ingredient_id && ingredient_id == that.ingredient_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(export_ingredient_id, ingredient_id);
    }
}