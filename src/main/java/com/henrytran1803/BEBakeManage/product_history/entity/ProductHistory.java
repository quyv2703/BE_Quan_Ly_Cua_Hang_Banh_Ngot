package com.henrytran1803.BEBakeManage.product_history.entity;

import com.henrytran1803.BEBakeManage.product.entity.Product;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_histories")
@Data
@NoArgsConstructor
public class ProductHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "effective_date")
    private LocalDateTime effectiveDate;

    @PrePersist
    protected void onCreate() {
        if (effectiveDate == null) {
            effectiveDate = LocalDateTime.now();
        }
    }
}
