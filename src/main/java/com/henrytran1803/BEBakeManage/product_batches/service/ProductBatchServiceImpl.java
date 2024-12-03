package com.henrytran1803.BEBakeManage.product_batches.service;

import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.product.dto.ProductSummaryDTO;
import com.henrytran1803.BEBakeManage.product.entity.Product;
import com.henrytran1803.BEBakeManage.product.repository.ProductRepository;
import com.henrytran1803.BEBakeManage.product_batches.dto.ProductBatchDetailDTO;
import com.henrytran1803.BEBakeManage.product_batches.dto.ProductBatchSumaryDTO;
import com.henrytran1803.BEBakeManage.product_batches.entity.ProductBatch;
import com.henrytran1803.BEBakeManage.product_batches.repository.ProductBatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductBatchServiceImpl implements ProductBatchService{
    private final ProductBatchRepository productBatchRepository;
    private final ProductRepository productRepository;

    @Override
    public ApiResponse<List<ProductSummaryDTO>> getListProductBatch() {
        List<Product> products = productRepository.findAll();
        List<String> statuses = Arrays.asList("active", "near_expiry");
        List<ProductSummaryDTO> productSummaryDTOList = new ArrayList<>();
        for (Product product : products) {
            List<ProductBatch> productBatches = productBatchRepository.findByProductIdAndStatusIn(product.getId(), statuses);
            List<ProductBatchSumaryDTO> productBatchDTOs = productBatches.stream().map(batch -> {
                ProductBatchSumaryDTO dto = new ProductBatchSumaryDTO();
                dto.setId((long) batch.getId());
                dto.setStatus(batch.getStatus());
                dto.setQuantity(batch.getQuantity());
                dto.setDailyDiscount(batch.getDailyDiscount());
                dto.setDateExpiry(batch.getExpirationDate().toLocalDate());
                dto.setCountDown((int) ChronoUnit.DAYS.between(LocalDate.now(), batch.getExpirationDate().toLocalDate()));
                return dto;
            }).collect(Collectors.toList());
            ProductSummaryDTO summaryDTO = new ProductSummaryDTO();
            summaryDTO.setId((long) product.getId());
            summaryDTO.setName(product.getName());
            summaryDTO.setShelfLifeDays(product.getShelfLifeDays());
            summaryDTO.setTotalProductBatch(productBatches.size());
            summaryDTO.setTotalNearExpiry(
                    (int) productBatches.stream()
                            .filter(batch -> "NEAR_EXPIRY".equalsIgnoreCase(batch.getStatus()))
                            .count()
            );
            summaryDTO.setTotalExpiry(
                    (int) productBatches.stream()
                            .filter(batch -> "EXPIRED".equalsIgnoreCase(batch.getStatus()))
                            .count()
            );
            summaryDTO.setProductBatches(productBatchDTOs);
            productSummaryDTOList.add(summaryDTO);
        }
        return ApiResponse.success(productSummaryDTOList);
    }
    @Override
    public ApiResponse<List<ProductBatchDetailDTO>> getListProductBatchByStatues(List<String> statuses) {
        List<Object[]> results = productBatchRepository.findProductBatchDetailsByStatuses(statuses);

        // Map the results to ProductBatchDetailDTO
        List<ProductBatchDetailDTO> dtos = results.stream()
                .map(result -> {
                    ProductBatchDetailDTO dto = new ProductBatchDetailDTO();

                    // Set values for the DTO
                    dto.setId(((Number) result[0]).longValue());
                    dto.setName((String) result[1]);
                    dto.setStatus((String) result[2]);
                    dto.setQuantity(((Number) result[3]).intValue());

                    // Set the dateExpiry based on the type of the result
                    if (result[4] instanceof java.sql.Timestamp) {
                        dto.setDateExpiry(((java.sql.Timestamp) result[4]).toLocalDateTime().toLocalDate());
                    } else if (result[4] instanceof java.sql.Date) {
                        dto.setDateExpiry(((java.sql.Date) result[4]).toLocalDate());
                    }

                    // Set countDown value
                    dto.setCountDown(((Number) result[5]).intValue());


                    Integer dailyDiscount = productBatchRepository.findDailyDiscountByProductBatchId(dto.getId());
                    dto.setDailyDiscount(dailyDiscount != null ? dailyDiscount : 0);

                    return dto;
                })
                .collect(Collectors.toList());

        return ApiResponse.success(dtos);
    }
    @Override
    public long countByStatus(String status) {
        return productBatchRepository.countByStatus(status); // Truy vấn trong repository
    }


}
