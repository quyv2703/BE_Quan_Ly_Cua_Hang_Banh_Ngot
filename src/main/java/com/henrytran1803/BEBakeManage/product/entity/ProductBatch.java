package com.henrytran1803.BEBakeManage.product.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "product_batches")
public class ProductBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "daily_production_id")
    private DailyProductInventory dailyProduction;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "current_discount")
    private Integer currentDiscount;

    @Column(name = "quantity")
    private Integer quantity;

    // Getters and Setters
}