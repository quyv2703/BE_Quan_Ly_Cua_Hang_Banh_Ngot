package com.henrytran1803.BEBakeManage.product_batches.entity;

import com.henrytran1803.BEBakeManage.daily_productions.entity.DailyProduction;
import com.henrytran1803.BEBakeManage.product.entity.Product;
import com.henrytran1803.BEBakeManage.promotion.entity.PromotionDetail;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
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

    @ManyToOne
    @JoinColumn(name = "daily_production_id")
    private DailyProduction dailyProduction;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;


    @Column(name = "daily_discount")
    private Integer dailyDiscount;

    @Column(name = "status")
    private String status;

    @Column(name = "quantity")
    private Integer quantity;

    // Thêm mối quan hệ với PromotionDetail
    @OneToMany(mappedBy = "productBatch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PromotionDetail> promotionDetails;
}
