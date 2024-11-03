package com.henrytran1803.BEBakeManage.promotion.repository;

import com.henrytran1803.BEBakeManage.promotion.entity.PromotionDetail;
import com.henrytran1803.BEBakeManage.promotion.entity.PromotionDetailId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PromotionDetailRepository extends JpaRepository<PromotionDetail, PromotionDetailId> {
    List<PromotionDetail> findByPromotionId(Integer promotionId);

    boolean existsByProductBatchId(Integer productId);

    Optional<Object> findByPromotionIdAndProductBatchId(Integer promotionId, Integer productId);

    void deleteByPromotionId(Integer id);
}
