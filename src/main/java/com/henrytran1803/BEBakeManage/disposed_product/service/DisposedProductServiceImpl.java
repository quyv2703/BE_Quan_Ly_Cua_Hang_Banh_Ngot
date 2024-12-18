package com.henrytran1803.BEBakeManage.disposed_product.service;

import com.henrytran1803.BEBakeManage.disposed_product.dto.DisposedBatchDTO;
import com.henrytran1803.BEBakeManage.disposed_product.dto.DisposedProductDTO;
import com.henrytran1803.BEBakeManage.disposed_product.dto.DisposedProductDetailResponseDTO;
import com.henrytran1803.BEBakeManage.disposed_product.dto.DisposedProductSummaryDTO;
import com.henrytran1803.BEBakeManage.disposed_product.entity.DisposedProduct;
import com.henrytran1803.BEBakeManage.disposed_product.entity.DisposedProductDetail;
import com.henrytran1803.BEBakeManage.disposed_product.repository.DisposedProductDetailRepository;
import com.henrytran1803.BEBakeManage.disposed_product.repository.DisposedProductRepository;
import com.henrytran1803.BEBakeManage.product.repository.ProductRepository;
import com.henrytran1803.BEBakeManage.product_batches.entity.ProductBatch;
import com.henrytran1803.BEBakeManage.product_batches.repository.ProductBatchRepository;
import com.henrytran1803.BEBakeManage.user.entity.User;
import com.henrytran1803.BEBakeManage.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DisposedProductServiceImpl implements DisposedProductService {
    private final UserRepository userRepository;
    private final DisposedProductDetailRepository disposedProductDetailRepository;
    private final DisposedProductRepository disposedProductRepository;
    private final ProductBatchRepository productBatchRepository;

    @Override
    public Boolean disposedProduct(DisposedProductDTO disposedProductDTO) {
        DisposedProduct disposedProduct = new DisposedProduct();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int id = Integer.parseInt((String) authentication.getPrincipal());
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        disposedProduct.setStaff(user.get());
        disposedProduct.setDateDisposed(LocalDateTime.now());
        disposedProduct.setNote(disposedProductDTO.getNote());

        disposedProduct = disposedProductRepository.save(disposedProduct);

        for (Integer productBatchId : disposedProductDTO.getProductBatchIds()) {
            Optional<ProductBatch> productBatchOpt = productBatchRepository.findById(productBatchId);

            if (productBatchOpt.isEmpty()) {
                throw new IllegalArgumentException("Product batch not found for ID: " + productBatchId);
            }

            ProductBatch productBatch = productBatchOpt.get();
            if (productBatch.getQuantity() < 0 || "DISPOSED".equals(productBatch.getStatus())) {
                throw new IllegalArgumentException(
                        "Product batch is already disposed or has no quantity remaining: ID " + productBatchId
                );
            }
            DisposedProductDetail disposedProductDetail = new DisposedProductDetail();
            disposedProductDetail.setDisposedProduct(disposedProduct);
            disposedProductDetail.setProductBatch(productBatch);
            disposedProductDetail.setDisposedQuantity(productBatch.getQuantity());
            disposedProductDetailRepository.save(disposedProductDetail);
            productBatch.setQuantity(0);
            productBatch.setStatus("DISPOSED");
            productBatchRepository.save(productBatch);
        }

        return true;
    }
    @Override
    public List<DisposedProductSummaryDTO> getAllDisposedProducts() {
        List<DisposedProduct> disposedProducts = disposedProductRepository.findAll();
        return disposedProducts.stream().map(dp -> {
            DisposedProductSummaryDTO dto = new DisposedProductSummaryDTO();
            dto.setId(dp.getId());
            dto.setDateDisposed(dp.getDateDisposed());
            dto.setNote(dp.getNote());
            dto.setStaffName(dp.getStaff().getFirstName()+" " +dp.getStaff().getLastName() );

            List<DisposedProductDetail> details = disposedProductDetailRepository
                    .findByDisposedProductId(dp.getId());
            dto.setTotalBatches(details.size());
            dto.setTotalQuantityDisposed(
                    details.stream()
                            .mapToInt(DisposedProductDetail::getDisposedQuantity)
                            .sum()
            );

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public DisposedProductDetailResponseDTO getDisposedProductById(int id) {
        DisposedProduct disposedProduct = disposedProductRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Disposed product not found"));

        List<DisposedProductDetail> details = disposedProductDetailRepository
                .findByDisposedProductId(id);

        DisposedProductDetailResponseDTO dto = new DisposedProductDetailResponseDTO();
        dto.setId(disposedProduct.getId());
        dto.setDateDisposed(disposedProduct.getDateDisposed());
        dto.setNote(disposedProduct.getNote());
        dto.setStaffName(disposedProduct.getStaff().getFirstName()+" " +disposedProduct.getStaff().getLastName() );

        List<DisposedBatchDTO> batchDTOs = details.stream().map(detail -> {
            DisposedBatchDTO batchDTO = new DisposedBatchDTO();
            batchDTO.setBatchId(detail.getProductBatch().getId());
            batchDTO.setProductName(detail.getProductBatch().getProduct().getName());
            batchDTO.setDisposedQuantity(detail.getDisposedQuantity());
            batchDTO.setManufacturingDate(detail.getProductBatch().getExpirationDate());
            return batchDTO;
        }).collect(Collectors.toList());

        dto.setDisposedBatches(batchDTOs);
        return dto;
    }

}
