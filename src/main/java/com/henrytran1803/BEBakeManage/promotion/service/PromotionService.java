package com.henrytran1803.BEBakeManage.promotion.service;

import com.henrytran1803.BEBakeManage.promotion.dto.CreatePromotionDTO;
import com.henrytran1803.BEBakeManage.promotion.dto.UpdatePromotionDTO;
import com.henrytran1803.BEBakeManage.promotion.entity.Promotion;
import com.henrytran1803.BEBakeManage.promotion.entity.PromotionDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PromotionService {
    List<Promotion> getAllActivePromotions();

    Page<Promotion> getAllPromotions(Pageable pageable);

    Promotion getPromotionById(Integer id);
    Promotion createPromotion(CreatePromotionDTO createPromotionDTO) throws Exception;
    Optional<Promotion> updatePromotionStatus(Integer promotionId, boolean status) throws Exception;
    void deletePromotionDetail(Integer promotionId, Integer productId) throws Exception;

    Promotion updatePromotion(Integer id, UpdatePromotionDTO updatePromotionDTO) throws Exception;
    void deletePromotion(Integer id) throws Exception;
    PromotionDetail addProductToPromotion(Integer promotionId, Integer productId) throws Exception;
    List<PromotionDetail> getPromotionDetails(Integer promotionId) throws Exception;
    Page<Promotion> searchPromotions(String keyword, Pageable pageable);
    boolean isProductInPromotion(Integer productId);
}
