package com.henrytran1803.BEBakeManage.product.repository;

import com.henrytran1803.BEBakeManage.product.dto.ProductDetailProjection;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ProductDetailsRepositoryImpl implements ProductDetailsRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ProductDetailProjection> findAllActiveProducts() {
        String sql = "SELECT * FROM v_product_details";
        Query query = entityManager.createNativeQuery(sql, "ProductDetailMapping");
        return query.getResultList();
    }
}