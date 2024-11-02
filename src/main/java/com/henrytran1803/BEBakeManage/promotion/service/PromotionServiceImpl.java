package com.henrytran1803.BEBakeManage.promotion.service;

import com.henrytran1803.BEBakeManage.common.exception.error.ErrorCode;
import com.henrytran1803.BEBakeManage.product.entity.Product;
import com.henrytran1803.BEBakeManage.promotion.dto.CreatePromotionDTO;
import com.henrytran1803.BEBakeManage.promotion.dto.UpdatePromotionDTO;
import com.henrytran1803.BEBakeManage.promotion.entity.Promotion;
import com.henrytran1803.BEBakeManage.promotion.entity.PromotionDetail;
import com.henrytran1803.BEBakeManage.promotion.entity.PromotionDetailId;
import com.henrytran1803.BEBakeManage.promotion.repository.PromotionDetailRepository;
import com.henrytran1803.BEBakeManage.promotion.repository.PromotionRepository;
import com.henrytran1803.BEBakeManage.product.repository.ProductRepository;
import com.henrytran1803.BEBakeManage.promotion.service.PromotionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private PromotionDetailRepository promotionDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public Promotion createPromotion(CreatePromotionDTO createPromotionDTO) throws Exception {
        if (createPromotionDTO.getEndDate().isBefore(createPromotionDTO.getStartDate())) {
            throw new Exception("End date must be after start date");
        }
        Promotion promotion = new Promotion();
        promotion.setName(createPromotionDTO.getName());
        promotion.setDescription(createPromotionDTO.getDescription());
        promotion.setStartDate(createPromotionDTO.getStartDate());
        promotion.setDiscount(createPromotionDTO.getDiscount());
        promotion.setIsActive(true);
        promotion.setEndDate(createPromotionDTO.getEndDate());
        promotion.setCreatedAt(LocalDateTime.now());
        promotionRepository.save(promotion);
        for (Integer productId : createPromotionDTO.getProductIds()) {
            productRepository.findById(productId).ifPresentOrElse(product -> {
                PromotionDetail detail = new PromotionDetail();
                detail.setId(new PromotionDetailId(promotion.getId(), productId));
                detail.setPromotion(promotion);
                detail.setProduct(product);
                promotionDetailRepository.save(detail);
            }, () -> {
                throw new RuntimeException("Product with ID " + productId + " not found");
            });
        }
        return promotion;
    }
    @Override
    @Transactional
    public Optional<Promotion> updatePromotionStatus(Integer promotionId, boolean status) throws Exception {
        return Optional.ofNullable(promotionRepository.findById(promotionId).map(promotion -> {
            promotion.setIsActive(status);
            promotion.setUpdatedAt(LocalDateTime.now());
            promotionRepository.save(promotion);
            return promotion;
        }).orElseThrow(() -> new Exception("Promotion not found with ID: " + promotionId)));
    }

    @Override
    @Transactional
    public void deletePromotionDetail(Integer promotionId, Integer productId) throws Exception {
        PromotionDetailId detailId = new PromotionDetailId(promotionId, productId);
        PromotionDetail detail = promotionDetailRepository.findById(detailId)
                .orElseThrow(() -> new Exception("Promotion detail not found for given promotion and product IDs"));
        promotionDetailRepository.delete(detail);
    }
    @Transactional
    @Override
    public List<Promotion> getAllActivePromotions() {
        return promotionRepository.findByIsActiveTrue();
    }
    @Transactional
    @Override
    public Page<Promotion> getAllPromotions(Pageable pageable) {
        return promotionRepository.findAll(pageable);
    }
    @Transactional
    @Override
    public Promotion getPromotionById(Integer id) {
        return promotionRepository.findById(id).orElse(null);
    }
    @Override
    public List<PromotionDetail> getPromotionDetails(Integer promotionId) throws Exception {
        return promotionDetailRepository.findByPromotionId(promotionId);
    }
    @Override
    public Page<Promotion> searchPromotions(String keyword, Pageable pageable) {
        return promotionRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    @Override
    public boolean isProductInPromotion(Integer productId) {
        return promotionDetailRepository.existsByProductId(productId);
    }

    @Override
    @Transactional
    public Promotion updatePromotion(Integer id, UpdatePromotionDTO updatePromotionDTO) throws Exception {
        Promotion existingPromotion = getPromotionById(id);

        if (updatePromotionDTO.getName() != null) {
            existingPromotion.setName(updatePromotionDTO.getName());
        }
        if (updatePromotionDTO.getDescription() != null) {
            existingPromotion.setDescription(updatePromotionDTO.getDescription());
        }
        if (updatePromotionDTO.getStartDate() != null && updatePromotionDTO.getEndDate() != null) {
            validatePromotionDates(updatePromotionDTO.getStartDate(), updatePromotionDTO.getEndDate());
            // Convert Date to LocalDateTime
            existingPromotion.setStartDate(updatePromotionDTO.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            existingPromotion.setEndDate(updatePromotionDTO.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        if (updatePromotionDTO.getDiscount() != null) {
            existingPromotion.setDiscount(updatePromotionDTO.getDiscount().intValue()); // If discount is a Double
        }
        return promotionRepository.save(existingPromotion);
    }


    private void validatePromotionDates(Date startDate, Date endDate) throws Exception {
        Date currentDate = new Date();
        if (startDate.after(endDate)) {
            throw new Exception(ErrorCode.PROMOTION_DATE_INVALID.getMessage());
        }
        if (startDate.before(currentDate)) {
            throw new Exception(ErrorCode.PROMOTION_DATE_INVALID.getMessage());
        }
    }
    @Override
    @Transactional
    public void deletePromotion(Integer id) throws Exception {
        Promotion promotion = getPromotionById(id);
        promotionDetailRepository.deleteByPromotionId(id);
        promotionRepository.delete(promotion);
    }

    @Override
    @Transactional
    public PromotionDetail addProductToPromotion(Integer promotionId, Integer productId) throws Exception {
        Promotion promotion = getPromotionById(promotionId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException(ErrorCode.PRODUCT_NOT_FOUND.getMessage()));

        if (promotionDetailRepository.findByPromotionIdAndProductId(promotionId, productId).isPresent()) {
            throw new RuntimeException(ErrorCode.PROMOTION_PRODUCT_ALREADY_EXISTS.getMessage());
        }

        PromotionDetail detail = new PromotionDetail();
        detail.setPromotion(promotion);
        detail.setProduct(product);


        return promotionDetailRepository.save(detail);
    }
}
