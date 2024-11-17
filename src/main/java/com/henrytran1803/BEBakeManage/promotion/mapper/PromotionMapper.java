package com.henrytran1803.BEBakeManage.promotion.mapper;

import com.henrytran1803.BEBakeManage.promotion.entity.Promotion;
import com.henrytran1803.BEBakeManage.promotion.dto.PromotionSearchResponse;
import org.springframework.stereotype.Component;

@Component
public class PromotionMapper {
    public PromotionSearchResponse toSearchResponse(Promotion promotion) {
        PromotionSearchResponse response = new PromotionSearchResponse();
        response.setId(promotion.getId());
        response.setName(promotion.getName());
        response.setDescription(promotion.getDescription());
        response.setDiscount(promotion.getDiscount());
        response.setIsActive(promotion.getIsActive());
        response.setStartDate(promotion.getStartDate());
        response.setEndDate(promotion.getEndDate());
        response.calculateRemainingDays();
        return response;
    }
}