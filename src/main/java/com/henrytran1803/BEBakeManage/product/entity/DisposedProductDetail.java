package com.henrytran1803.BEBakeManage.product.entity;

import com.henrytran1803.BEBakeManage.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "disposed_product_details")
public class DisposedProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "disposed_quantity")
    private int disposedQuantity;


    @ManyToOne
    @JoinColumn(name = "product_batch_id")
    private ProductBatch productBatch;


    @ManyToOne
    @JoinColumn(name = "disposed_product_id")
    private DisposedProduct disposedProduct;


}
