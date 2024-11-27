package com.henrytran1803.BEBakeManage.import_ingredients.dto;

import java.util.List;

public class ImportIngredientRequest {
    private int user_id;
    private Integer id_supplier;
    private List<ImportIngredientDetailRequest> ingredients;  
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public Integer getId_supplier() {
		return id_supplier;
	}
	public void setId_supplier(Integer id_supplier) {
		this.id_supplier = id_supplier;
	}
	public List<ImportIngredientDetailRequest> getIngredients() {
		return ingredients;
	}
	public void setIngredients(List<ImportIngredientDetailRequest> ingredients) {
		this.ingredients = ingredients;
	} 
}