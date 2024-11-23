package com.henrytran1803.BEBakeManage.export_ingredients.dto;

public class ExportIngredientProductDetailRequest {
	private int product_id;
    private double quantity;

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public double getQuantity() {
		return quantity;
	}
	
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	} 
}
