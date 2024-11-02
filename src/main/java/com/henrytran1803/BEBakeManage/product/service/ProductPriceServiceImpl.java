package com.henrytran1803.BEBakeManage.product.service;

import com.henrytran1803.BEBakeManage.product.dto.ProductHistoryDTO;
import com.henrytran1803.BEBakeManage.product.entity.Product;
import com.henrytran1803.BEBakeManage.product.entity.ProductHistory;
import com.henrytran1803.BEBakeManage.product.repository.ProductHistoryRepository;
import com.henrytran1803.BEBakeManage.product.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductPriceServiceImpl implements ProductPriceService{
    private final ProductRepository productRepository;
    private final ProductHistoryRepository productHistoryRepository;
    private final EntityManager entityManager;
    @Transactional
    public void updateProductPrice(Integer productId, Double newPrice) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductHistory priceHistory = new ProductHistory();
        priceHistory.setProduct(product);
        priceHistory.setPrice(newPrice);
        priceHistory.setEffectiveDate(LocalDateTime.now());

        product.addProductHistory(priceHistory);
        productHistoryRepository.save(priceHistory);
    }
    public Double getCurrentPrice(Integer productId) {
        return productHistoryRepository.findTopByProductIdOrderByEffectiveDateDesc(productId)
                .map(ProductHistory::getPrice)
                .orElse(null);
    }

    @Transactional
    public void updateAllProductsPrices() {
        entityManager.createNativeQuery("CALL update_all_products_current_price()")
                .executeUpdate();
    }
    @Transactional
    @Override
    public List<ProductHistoryDTO> getAllProductHistoryByIdProduct(int idProduct) {
        List<ProductHistory> histories = productHistoryRepository.findByProductIdOrderByEffectiveDateDesc(idProduct);
        return histories.stream()
                .map(ProductHistoryDTO::new)
                .collect(Collectors.toList());
    }
}
