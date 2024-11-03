package com.henrytran1803.BEBakeManage.promotion.controller;

import com.henrytran1803.BEBakeManage.common.exception.error.ErrorCode;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.promotion.dto.CreatePromotionDTO;
import com.henrytran1803.BEBakeManage.promotion.dto.PromotionDetailDTO;
import com.henrytran1803.BEBakeManage.promotion.dto.UpdatePromotionDTO;
import com.henrytran1803.BEBakeManage.promotion.entity.Promotion;
import com.henrytran1803.BEBakeManage.promotion.entity.PromotionDetail;
import com.henrytran1803.BEBakeManage.promotion.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;
    @PostMapping("/{promotionId}/products")
    public ResponseEntity<ApiResponse<PromotionDetail>> addProductToPromotion(
            @PathVariable Integer promotionId,
            @RequestBody PromotionDetailDTO detailDTO) {
        try {
            PromotionDetail detail = promotionService.addProductToPromotion(
                    promotionId,
                    detailDTO.getProductBatchId());
                    return ResponseEntity.ok(ApiResponse.success(detail));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(
                            e.getMessage(),
                            ErrorCode.PROMOTION_PRODUCT_ALREADY_EXISTS.getCode()));
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Promotion>> updatePromotion(
            @PathVariable Integer id,
            @RequestBody UpdatePromotionDTO updatePromotionDTO) {
        try {
            Promotion updatedPromotion = promotionService.updatePromotion(id, updatePromotionDTO);
            return ResponseEntity.ok(ApiResponse.success(updatedPromotion));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(
                            ErrorCode.PROMOTION_UPDATE_FAILED.getMessage(),
                            ErrorCode.PROMOTION_UPDATE_FAILED.getCode()));
        }
    }
    @GetMapping("/{promotionId}/products")
    public ResponseEntity<ApiResponse<List<PromotionDetail>>> getPromotionDetails(
            @PathVariable Integer promotionId) {
        try {
            List<PromotionDetail> details = promotionService.getPromotionDetails(promotionId);
            return ResponseEntity.ok(ApiResponse.success(details));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(
                            ErrorCode.PROMOTION_NOT_FOUND.getMessage(),
                            ErrorCode.PROMOTION_NOT_FOUND.getCode()));
        }
    }
    
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<Promotion>>> getActivePromotions() {
        try {
            List<Promotion> activePromotions = promotionService.getAllActivePromotions();
            if (activePromotions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(
                                ErrorCode.PROMOTION_NOT_ACTIVE.getMessage(),
                                ErrorCode.PROMOTION_NOT_ACTIVE.getCode()));
            }
            return ResponseEntity.ok(ApiResponse.success(activePromotions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),
                            ErrorCode.INTERNAL_SERVER_ERROR.getCode()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Promotion>>> getAllPromotions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Promotion> promotions = promotionService.getAllPromotions(pageable);
            if (promotions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(
                                ErrorCode.PROMOTION_NOT_FOUND.getMessage(),
                                ErrorCode.PROMOTION_NOT_FOUND.getCode()));
            }
            return ResponseEntity.ok(ApiResponse.success(promotions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),
                            ErrorCode.INTERNAL_SERVER_ERROR.getCode()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Promotion>> getPromotionById(@PathVariable Integer id) {
        try {
            Promotion promotion = promotionService.getPromotionById(id);
            if (promotion == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(
                                ErrorCode.PROMOTION_NOT_FOUND.getMessage(),
                                ErrorCode.PROMOTION_NOT_FOUND.getCode()));
            }
            return ResponseEntity.ok(ApiResponse.success(promotion));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),
                            ErrorCode.INTERNAL_SERVER_ERROR.getCode()));
        }
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<Promotion>> createPromotion(@RequestBody CreatePromotionDTO createPromotionDTO) {
        try {
            // Validate promotion data
            if (!isValidPromotionData(createPromotionDTO)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(
                                ErrorCode.INVALID_PROMOTION_DATA.getMessage(),
                                ErrorCode.INVALID_PROMOTION_DATA.getCode()));
            }

            // Validate dates
            if (!isValidPromotionDates(createPromotionDTO)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(
                                ErrorCode.PROMOTION_DATE_INVALID.getMessage(),
                                ErrorCode.PROMOTION_DATE_INVALID.getCode()));
            }

            // Validate discount
            if (!isValidDiscount(createPromotionDTO)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(
                                ErrorCode.PROMOTION_DISCOUNT_INVALID.getMessage(),
                                ErrorCode.PROMOTION_DISCOUNT_INVALID.getCode()));
            }

            Promotion promotion = promotionService.createPromotion(createPromotionDTO);
            return ResponseEntity.ok(ApiResponse.success(promotion));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(
                            ErrorCode.PROMOTION_CREATE_FAILED.getMessage(),
                            ErrorCode.PROMOTION_CREATE_FAILED.getCode()));
        }
    }

    @PutMapping("/{promotionId}/status")
    public ResponseEntity<ApiResponse<Promotion>> updatePromotionStatus(
            @PathVariable Integer promotionId,
            @RequestParam boolean status) {
        try {
            Optional<Promotion> updatedPromotion = promotionService.updatePromotionStatus(promotionId, status);
            if (updatedPromotion.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(
                                ErrorCode.PROMOTION_NOT_FOUND.getMessage(),
                                ErrorCode.PROMOTION_NOT_FOUND.getCode()));
            }
            return ResponseEntity.ok(ApiResponse.success(updatedPromotion.get()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(
                            ErrorCode.PROMOTION_STATUS_UPDATE_FAILED.getMessage(),
                            ErrorCode.PROMOTION_STATUS_UPDATE_FAILED.getCode()));
        }
    }

    @DeleteMapping("/{promotionId}/products/{productId}")
    public ResponseEntity<ApiResponse<Void>> deletePromotionDetail(
            @PathVariable Integer promotionId,
            @PathVariable Integer productId) {
        try {
            // Check if promotion exists
            Promotion promotion = promotionService.getPromotionById(promotionId);
            if (promotion == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(
                                ErrorCode.PROMOTION_NOT_FOUND.getMessage(),
                                ErrorCode.PROMOTION_NOT_FOUND.getCode()));
            }

            // Check if product exists in promotion
            if (!promotionHasProduct(promotion, productId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(
                                ErrorCode.PROMOTION_PRODUCT_NOT_FOUND.getMessage(),
                                ErrorCode.PROMOTION_PRODUCT_NOT_FOUND.getCode()));
            }

            promotionService.deletePromotionDetail(promotionId, productId);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(
                            ErrorCode.PROMOTION_DETAIL_DELETE_FAILED.getMessage(),
                            ErrorCode.PROMOTION_DETAIL_DELETE_FAILED.getCode()));
        }
    }
    private boolean isValidPromotionData(CreatePromotionDTO dto) {
        return dto != null &&
                dto.getName() != null && !dto.getName().trim().isEmpty() &&
                dto.getStartDate() != null &&
                dto.getEndDate() != null;
    }

    private boolean isValidPromotionDates(CreatePromotionDTO dto) {
        LocalDateTime now = LocalDateTime.now();
        return dto.getStartDate().isBefore(dto.getEndDate()) &&
                dto.getStartDate().isAfter(now);
    }


    private boolean isValidDiscount(CreatePromotionDTO dto) {
        return dto.getDiscount() > 0 && dto.getDiscount() <= 100;
    }

    private boolean promotionHasProduct(Promotion promotion, Integer productId) {
        return true;
    }
}