package com.henrytran1803.BEBakeManage.ingredients.dto;

public class IngredientCreationRequest {
	private String name;
	private int unit_id;
	private double warning_limits;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(int unit_id) {
		this.unit_id = unit_id;
	}
	public double getWarning_limits() {
		return warning_limits;
	}
	public void setWarning_limits(double warning_limits) {
		this.warning_limits = warning_limits;
	}
}
