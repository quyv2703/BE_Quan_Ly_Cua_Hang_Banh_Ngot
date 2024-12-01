package com.henrytran1803.BEBakeManage.promotion.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.henrytran1803.BEBakeManage.product_batches.entity.ProductBatch;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "promotion_details")
@Data
@NoArgsConstructor
@Setter
@Getter
public class PromotionDetail {

    @EmbeddedId
    private PromotionDetailId id;

    @ManyToOne
    @MapsId("promotionId")
    @JsonBackReference
    @JoinColumn(name = "promotion_id", referencedColumnName = "id", nullable = false)
    private Promotion promotion;

    @ManyToOne
    @MapsId("productBatchId")
    @JsonBackReference
    @JoinColumn(name = "product_batch_id", referencedColumnName = "id", nullable = false)
    private ProductBatch productBatch;
}
