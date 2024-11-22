package com.henrytran1803.BEBakeManage.promotion.repository;

import com.henrytran1803.BEBakeManage.promotion.entity.PromotionDetail;
import com.henrytran1803.BEBakeManage.promotion.entity.PromotionDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PromotionDetailRepository extends JpaRepository<PromotionDetail, PromotionDetailId> {
    List<PromotionDetail> findByPromotionId(Integer promotionId);

    boolean existsByProductBatchId(Integer productId);

    Optional<Object> findByPromotionIdAndProductBatchId(Integer promotionId, Integer productId);

    @Modifying
    @Query("DELETE FROM PromotionDetail pd WHERE pd.promotion.id = :promotionId AND pd.productBatch.id = :productId")
    void deleteByPromotionIdAndProductBatchId(@Param("promotionId") Integer promotionId, @Param("productId") Integer productId);
    @Query("SELECT CASE WHEN COUNT(pd) > 0 THEN true ELSE false END FROM PromotionDetail pd " +
            "WHERE pd.id.promotionId = :promotionId AND pd.id.productBatchId = :productBatchId")
    boolean existsByPromotionIdAndProductBatchId(Integer promotionId, Integer productBatchId);


}
