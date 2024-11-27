package com.henrytran1803.BEBakeManage.import_ingredients.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ImportIngredientResponse {
    private int id;
    private int user_id;
    private Integer id_supplier;
    private LocalDateTime import_date;
    private double total_amount;
    private List<ImportIngredientDetailRequest> ingredients;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public LocalDateTime getImport_date() {
        return import_date;
    }

    public void setImport_date(LocalDateTime import_date) {
        this.import_date = import_date;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public List<ImportIngredientDetailRequest> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<ImportIngredientDetailRequest> ingredients) {
        this.ingredients = ingredients;
    }
}