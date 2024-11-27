package com.henrytran1803.BEBakeManage.import_ingredients.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "import_ingredients")
public class ImportIngredient {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int user_id;

    private LocalDateTime import_date = LocalDateTime.now(); 

    private double total_amount;

    private Integer id_supplier;

    @OneToMany(mappedBy = "importIngredient", cascade = CascadeType.ALL)
    private List<ImportIngredientDetail> details;

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

	public Integer getId_supplier() {
		return id_supplier;
	}

	public void setId_supplier(Integer id_supplier) {
		this.id_supplier = id_supplier;
	}

	public List<ImportIngredientDetail> getDetails() {
		return details;
	}

	public void setDetails(List<ImportIngredientDetail> details) {
		this.details = details;
	}
}
