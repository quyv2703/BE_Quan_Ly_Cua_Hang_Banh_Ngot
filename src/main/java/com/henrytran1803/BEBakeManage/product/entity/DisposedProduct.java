package com.henrytran1803.BEBakeManage.product.entity;

import com.henrytran1803.BEBakeManage.common.DisposalReason;
import com.henrytran1803.BEBakeManage.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "disposed_products")
public class DisposedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "disposed_quantity", nullable = false)
    private int disposedQuantity;

    @ManyToOne
    @JoinColumn(name = "product_batch_id")
    private ProductBatch productBatch;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason", nullable = false)
    private DisposalReason reason;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private User staff;

    @Column(name = "date_disposed", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dateDisposed;

    // Getters and Setters
}

