package com.henrytran1803.BEBakeManage.export_ingredients.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ExportIngredientResponse {
    private int id;
    private int sender_id;
    private LocalDateTime export_date;
    private int daily_production_id;
    private double total_amount;
    private List<ExportIngredientDetailRequest> ingredients;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSender_id() {
		return sender_id;
	}
	public void setSender_id(int sender_id) {
		this.sender_id = sender_id;
	}
	public LocalDateTime getExport_date() {
		return export_date;
	}
	public void setExport_date(LocalDateTime export_date) {
		this.export_date = export_date;
	}
	public int getDaily_production_id() {
		return daily_production_id;
	}
	public void setDaily_production_id(int daily_production_id) {
		this.daily_production_id = daily_production_id;
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
}