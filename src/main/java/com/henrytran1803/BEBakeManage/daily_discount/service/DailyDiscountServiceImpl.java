package com.henrytran1803.BEBakeManage.daily_discount.service;

import com.henrytran1803.BEBakeManage.daily_discount.dto.CreateDailyDiscount;
import com.henrytran1803.BEBakeManage.daily_discount.entity.DailyDiscount;
import com.henrytran1803.BEBakeManage.daily_discount.repository.DailyDiscountRepository;
import com.henrytran1803.BEBakeManage.product_batches.entity.ProductBatch;
import com.henrytran1803.BEBakeManage.product_batches.repository.ProductBatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
@Service
@RequiredArgsConstructor
public class DailyDiscountServiceImpl implements DailyDiscountService{
    private ProductBatchRepository productBatchRepository;
    private DailyDiscountRepository dailyDiscountRepository;
    @Autowired
    public DailyDiscountServiceImpl(ProductBatchRepository productBatchRepository,
                                    DailyDiscountRepository dailyDiscountRepository) {
        this.productBatchRepository = productBatchRepository;
        this.dailyDiscountRepository = dailyDiscountRepository;
    }
    @Override
    public Boolean createPromotionQuick(CreateDailyDiscount dto) {
        if (dto.getGetLastestDate()) {
            dto.setEndDate(productBatchRepository.findMaxExpiryDateByBatchIds(dto.getProductBatchIds()));
        }

        for (Integer productBatchId : dto.getProductBatchIds()) {
            System.out.println(productBatchId);
            double discount = dto.getSkipDefaultDiscount()
                    ? productBatchRepository.findDiscountLimitByProductBatchId(productBatchId)
                    : dto.getDiscount();

            DailyDiscount detail = new DailyDiscount();
            ProductBatch productBatch = productBatchRepository.findById(productBatchId)
                    .orElseThrow(() -> new IllegalArgumentException("ProductBatch not found for ID: " + productBatchId));

            detail.setProductBatch(productBatch);
            detail.setDiscount((int) discount);
            detail.setStartDate(LocalDate.now());
            detail.setEndDate(dto.getEndDate().toLocalDate());
            productBatch.setDailyDiscount((int) discount);
            productBatchRepository.save(productBatch);
            dailyDiscountRepository.save(detail);
        }

        return true;
    }
}
