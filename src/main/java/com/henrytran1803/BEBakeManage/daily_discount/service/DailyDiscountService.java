package com.henrytran1803.BEBakeManage.daily_discount.service;

import com.henrytran1803.BEBakeManage.daily_discount.dto.CreateDailyDiscount;

public interface DailyDiscountService {
    Boolean createPromotionQuick(CreateDailyDiscount createPromotionQuickDTO);
}
