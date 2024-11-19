package com.henrytran1803.BEBakeManage.promotion.repository;

import com.henrytran1803.BEBakeManage.promotion.entity.DailyDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyDiscountRepository extends JpaRepository<DailyDiscount, Integer> {
}
