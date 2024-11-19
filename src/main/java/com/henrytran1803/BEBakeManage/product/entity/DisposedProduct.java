package com.henrytran1803.BEBakeManage.product.entity;

import com.henrytran1803.BEBakeManage.common.DisposalReason;
import com.henrytran1803.BEBakeManage.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "disposed_products")
public class DisposedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private User staff;

    @Column(name = "date_disposed", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dateDisposed;
}

