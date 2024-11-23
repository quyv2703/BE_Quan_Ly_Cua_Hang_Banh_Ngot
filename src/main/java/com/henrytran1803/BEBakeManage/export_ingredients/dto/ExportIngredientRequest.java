package com.henrytran1803.BEBakeManage.export_ingredients.dto;

import java.util.List;

public class ExportIngredientRequest {
    private int sender_id;
    private double total_amount;
    private List<ExportIngredientDetailRequest> ingredients;
    private List<ExportIngredientProductDetailRequest> products;
    
    public int getSender_id() {
		return sender_id;
	}
	
    public void setSender_id(int sender_id) {
		this.sender_id = sender_id;
	}
    
    public double getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(double total_amount) {
		this.total_amount = total_amount;
	}

	public List<ExportIngredientDetailRequest> getIngredients() {
		return ingredients;
	}
	
    public void setIngredients(List<ExportIngredientDetailRequest> ingredients) {
		this.ingredients = ingredients;
	}  
    
    public List<ExportIngredientProductDetailRequest> getProducts() {
        return products;
    }

    public void setProducts(List<ExportIngredientProductDetailRequest> products) {
        this.products = products;
    }
}