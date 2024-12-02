package com.henrytran1803.BEBakeManage.product_history.repository;

import com.henrytran1803.BEBakeManage.product_history.entity.ProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Integer> {
    Optional<ProductHistory> findTopByProductIdOrderByEffectiveDateDesc(Integer productId);

    List<ProductHistory> findByProductIdOrderByEffectiveDateDesc(Integer productId);
}