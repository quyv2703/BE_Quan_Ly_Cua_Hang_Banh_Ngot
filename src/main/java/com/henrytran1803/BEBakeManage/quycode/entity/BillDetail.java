package com.henrytran1803.BEBakeManage.quycode.entity;


import com.henrytran1803.BEBakeManage.product.entity.ProductBatch;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bill_details")
@Data
@NoArgsConstructor
public class BillDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill; // Liên kết với Bill

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_batch_id", nullable = false)
    private ProductBatch productBatch; // Lô bánh mà khách chọn

    @Column(name = "quantity", nullable = false)
    private Integer quantity; // Số lượng sản phẩm

    @Column(name = "price", nullable = false)
    private Double price; // Giá mỗi sản phẩm tại thời điểm đặt hàng
}
