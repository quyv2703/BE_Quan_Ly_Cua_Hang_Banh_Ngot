package com.henrytran1803.BEBakeManage.import_ingredients.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class ImportIngredientDetailId implements Serializable{
	
	//Thêm serialVersionUID
    private static final long serialVersionUID = 1L;
    
	private int import_ingredient_id;
    private int ingredient_id;

    public ImportIngredientDetailId() {}

    public ImportIngredientDetailId(int import_ingredient_id, int ingredient_id) {
        this.import_ingredient_id = import_ingredient_id;
        this.ingredient_id = ingredient_id;
    }
    
    public int getImport_ingredient_id() {
		return import_ingredient_id;
	}

	public void setImport_ingredient_id(int import_ingredient_id) {
		this.import_ingredient_id = import_ingredient_id;
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
        ImportIngredientDetailId that = (ImportIngredientDetailId) o;
        return import_ingredient_id == that.import_ingredient_id && ingredient_id == that.ingredient_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(import_ingredient_id, ingredient_id);
    }
}
