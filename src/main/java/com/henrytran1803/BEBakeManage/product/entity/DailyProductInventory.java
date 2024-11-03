package com.henrytran1803.BEBakeManage.product.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "daily_product_inventories")
public class DailyProductInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "product_batch_id")
    private ProductBatch productBatch;

    @Column(name = "inventory_date", nullable = false)
    private LocalDateTime inventoryDate;

    @Column(name = "quantity")
    private Integer quantity;

    // Getters and Setters
}