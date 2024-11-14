package com.henrytran1803.BEBakeManage.Image.entity;
import com.henrytran1803.BEBakeManage.product.entity.Product;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "url", nullable = false, length = 250)
    private String url;


}
