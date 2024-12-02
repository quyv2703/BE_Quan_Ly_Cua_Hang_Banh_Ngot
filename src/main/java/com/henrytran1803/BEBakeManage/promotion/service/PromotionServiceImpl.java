package com.henrytran1803.BEBakeManage.promotion.service;

import com.henrytran1803.BEBakeManage.common.exception.error.ErrorCode;
import com.henrytran1803.BEBakeManage.daily_discount.dto.CreateDailyDiscount;
import com.henrytran1803.BEBakeManage.product_batches.entity.ProductBatch;
import com.henrytran1803.BEBakeManage.product_batches.repository.ProductBatchRepository;
import com.henrytran1803.BEBakeManage.promotion.dto.*;
import com.henrytran1803.BEBakeManage.daily_discount.entity.DailyDiscount;
import com.henrytran1803.BEBakeManage.promotion.entity.Promotion;
import com.henrytran1803.BEBakeManage.promotion.entity.PromotionDetail;
import com.henrytran1803.BEBakeManage.promotion.entity.PromotionDetailId;
import com.henrytran1803.BEBakeManage.promotion.mapper.PromotionMapper;
import com.henrytran1803.BEBakeManage.daily_discount.repository.DailyDiscountRepository;
import com.henrytran1803.BEBakeManage.promotion.repository.PromotionDetailRepository;
import com.henrytran1803.BEBakeManage.promotion.repository.PromotionRepository;
import com.henrytran1803.BEBakeManage.product.repository.ProductRepository;
import com.henrytran1803.BEBakeManage.promotion.specification.PromotionSpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private DailyDiscountRepository dailyDiscountRepository;

    @Autowired
    private PromotionMapper promotionMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductBatchRepository productBatchRepository;
    @Override
    @Transactional
    public Promotion createPromotion(CreatePromotionDTO createPromotionDTO) throws Exception {
        if (createPromotionDTO.getEndDate().isBefore(createPromotionDTO.getStartDate())) {
            System.out.println("End date {} is before start date {}"+ createPromotionDTO.getEndDate() +createPromotionDTO.getStartDate());
            throw new IllegalArgumentException("End date must be after start date");
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
        System.out.println("Promotion saved with ID: {}"+ promotion.getId());

        for (Integer productBatchId : createPromotionDTO.getProductBatchIds()) {
            System.out.println("Processing productBatchId: {}"+ productBatchId);

            Optional<PromotionDetail> existingDetail = promotionDetailRepository.findById(
                    new PromotionDetailId(promotion.getId(), productBatchId)
            );

            if (existingDetail.isPresent()) {
                System.out.println("PromotionDetail already exists for promotionId={} and productBatchId={}"+ promotion.getId()+ productBatchId);
                continue; // Bỏ qua hoặc xử lý theo cách khác
            }

            productBatchRepository.findById(productBatchId).ifPresentOrElse(productBatch -> {
                PromotionDetail detail = new PromotionDetail();
                detail.setId(new PromotionDetailId(promotion.getId(), productBatchId));
                detail.setPromotion(promotion);
                detail.setProductBatch(productBatch);
                promotionDetailRepository.save(detail);
                System.out.println("Saved PromotionDetail for productBatchId: {}"+ productBatchId);
            }, () -> {
                System.out.println("Product with ID {} not found"+productBatchId);
                throw new RuntimeException("Product with ID " + productBatchId + " not found");
            });
        }

        System.out.println("Promotion created successfully: {}"+promotion);
        return promotion;
    }

    @Override
    @Transactional
    public Promotion updatePromotionStatus(Integer promotionId) throws Exception {
        try {
            Promotion promotion = promotionRepository.findById(promotionId)
                    .orElseThrow(() -> new Exception("Promotion not found with ID: " + promotionId));

            if (promotion.getIsActive()) {
                promotion.setIsActive(false);
            } else {
                promotion.setIsActive(true);
            }

            promotion.setUpdatedAt(LocalDateTime.now());

            promotionRepository.save(promotion);

            System.out.print(promotion.getId());

            return promotion;
        } catch (Exception error) {
            throw new Exception("Error while updating promotion status: " + error.getMessage(), error);
        }
    }

    @Override
    @Transactional
    public void deletePromotionDetail(Integer promotionId, Integer productId) throws Exception {
        if (!promotionDetailRepository.existsById(new PromotionDetailId(productId, promotionId))) {
            throw new Exception("Promotion detail not found");
        }
        promotionDetailRepository.deleteByPromotionIdAndProductBatchId(promotionId, productId);
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
        return promotionDetailRepository.existsByProductBatchId(productId);
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
//        promotionDetailRepository.deleteByPromotionId(id);
        promotionRepository.delete(promotion);
    }

    @Override
    @Transactional
    public PromotionDetail addProductToPromotion(Integer promotionId, Integer productBatchId) throws Exception {
        Promotion promotion = getPromotionById(promotionId);

        ProductBatch productBatch = productBatchRepository.findById(productBatchId)
                .orElseThrow(() -> new RuntimeException("Product batch not found"));

        if (promotionDetailRepository.existsByPromotionIdAndProductBatchId(promotionId, productBatchId)) {
            throw new RuntimeException("Promotion product already exists");
        }

        // Create and set the composite key
        PromotionDetailId detailId = new PromotionDetailId(productBatchId, promotionId);

        PromotionDetail detail = new PromotionDetail();
        detail.setId(detailId);  // Set the composite key
        detail.setPromotion(promotion);
        detail.setProductBatch(productBatch);

        return promotionDetailRepository.save(detail);
    }
    @Override
    public Page<PromotionSearchResponse> searchPromotions(PromotionSearchCriteria criteria) {
        Sort sort = Sort.by(
                criteria.getSortDir().equalsIgnoreCase("asc")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC,
                criteria.getSortBy()
        );

        PageRequest pageRequest = PageRequest.of(
                criteria.getPage(),
                criteria.getSize(),
                sort
        );

        Page<Promotion> promotionPage = promotionRepository.findAll(
                PromotionSpecification.getSpecification(criteria),
                pageRequest
        );

        // Map to DTO
        return promotionPage.map(promotionMapper::toSearchResponse);
    }


}
