package com.henrytran1803.BEBakeManage.import_ingredients.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "import_ingredient_details")
public class ImportIngredientDetail {
	@EmbeddedId
    private ImportIngredientDetailId id;
	
    @ManyToOne
    @MapsId("importIngredientId")
    @JoinColumn(name = "import_ingredient_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private ImportIngredient importIngredient;

    private double quantity;

    private double price;

    public ImportIngredientDetailId getId() {
        return id;
    }

    public void setId(ImportIngredientDetailId id) {
        this.id = id;
    }

    public ImportIngredient getImportIngredient() {
        return importIngredient;
    }

    public void setImportIngredient(ImportIngredient importIngredient) {
        this.importIngredient = importIngredient;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}