package com.henrytran1803.BEBakeManage.promotion.repository;

import com.henrytran1803.BEBakeManage.category.entity.Category;
import com.henrytran1803.BEBakeManage.promotion.entity.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PromotionRepository  extends JpaRepository<Promotion, Integer>, JpaSpecificationExecutor<Promotion> {
    List<Promotion> findByIsActiveTrue();
    Page<Promotion> findAll(Pageable pageable);
    Page<Promotion> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
