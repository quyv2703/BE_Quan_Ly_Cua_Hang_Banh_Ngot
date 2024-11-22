package com.henrytran1803.BEBakeManage.product.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "daily_discount")
    private Integer dailyDiscount;

    @Column(name = "current_discount")
    private Integer currentDiscount;

    @Column(name = "status")
    private String status;

    @Column(name = "quantity")
    private Integer quantity;

    @OneToMany(mappedBy = "productBatch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyProductInventory> dailyInventories = new ArrayList<>();
}
