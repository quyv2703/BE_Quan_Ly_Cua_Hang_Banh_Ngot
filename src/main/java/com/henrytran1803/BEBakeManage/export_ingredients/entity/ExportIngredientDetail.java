package com.henrytran1803.BEBakeManage.export_ingredients.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "export_ingredient_details")
public class ExportIngredientDetail {
    @EmbeddedId
    private ExportIngredientDetailId id;
    
    @ManyToOne
    @MapsId("exportIngredientId")
    @JoinColumn(name = "export_ingredient_id", referencedColumnName = "id",  insertable = false, updatable = false)
    @JsonIgnore
    private ExportIngredient exportIngredient;

    private double quantity;
    
    public ExportIngredientDetail() {}

    public ExportIngredientDetail(ExportIngredientDetailId id, ExportIngredient exportIngredient, double quantity) {
        this.id = id;
        this.exportIngredient = exportIngredient;
        this.quantity = quantity;
    }

    public ExportIngredientDetailId getId() {
        return id;
    }

    public void setId(ExportIngredientDetailId id) {
        this.id = id;
    }

    public ExportIngredient getExportIngredient() {
        return exportIngredient;
    }

    public void setExportIngredient(ExportIngredient exportIngredient) {
        this.exportIngredient = exportIngredient;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}