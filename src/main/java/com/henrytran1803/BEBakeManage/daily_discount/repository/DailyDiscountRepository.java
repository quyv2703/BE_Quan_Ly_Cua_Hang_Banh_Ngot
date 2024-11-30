package com.henrytran1803.BEBakeManage.daily_discount.repository;

import com.henrytran1803.BEBakeManage.daily_discount.entity.DailyDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyDiscountRepository extends JpaRepository<DailyDiscount, Integer> {
}
