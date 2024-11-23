package com.henrytran1803.BEBakeManage.export_ingredients.dto;


public class ExportIngredientDetailRequest {
    private int ingredient_id;
    private double quantity;

	public int getIngredient_id() {
		return ingredient_id;
	}

	public void setIngredient_id(int ingredient_id) {
		this.ingredient_id = ingredient_id;
	}

	public double getQuantity() {
		return quantity;
	}
	
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	} 

}