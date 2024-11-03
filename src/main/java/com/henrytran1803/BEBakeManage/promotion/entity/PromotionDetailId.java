package com.henrytran1803.BEBakeManage.promotion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class PromotionDetailId implements java.io.Serializable {
    @Column(name = "product_batch_id")
    private int productBatchId;

    @Column(name = "promotion_id")
    private int promotionId;

    public PromotionDetailId(int productBatchId, int promotionId) {
        this.productBatchId = productBatchId;
        this.promotionId = promotionId;
    }
}