package com.henrytran1803.BEBakeManage.promotion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class PromotionDetailId implements java.io.Serializable {
    @Column(name = "product_id")
    private int productId;

    @Column(name = "promotion_id")
    private int promotionId;

    public PromotionDetailId(int productId, int promotionId) {
        this.productId = productId;
        this.promotionId = promotionId;
    }
}