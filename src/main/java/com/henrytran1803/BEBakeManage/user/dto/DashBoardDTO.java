package com.henrytran1803.BEBakeManage.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashBoardDTO {
    private double todayRevenue;
    private int todayBill;
    private String productNameBestSeller;
    private List<MonthRevenueDTO> monthRevenue;
    private List<ProductCategoryRevenueDTO> categoryRevenue;
    private Map<String, Integer> hourlyOrders;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthRevenueDTO {
        private String month;
        private double revenue;
        private int totalOrders;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductCategoryRevenueDTO {
        private String categoryName;
        private double revenue;
        private double percentage;
    }
}