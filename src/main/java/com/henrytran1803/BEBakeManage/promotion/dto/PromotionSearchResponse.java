package com.henrytran1803.BEBakeManage.promotion.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
public class PromotionSearchResponse {
    private Integer id;
    private String name;
    private String description;
    private Integer discount;
    private Boolean isActive;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long remainingDays;

    public void calculateRemainingDays() {
        if (this.endDate != null) {
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(endDate)) {
                this.remainingDays = ChronoUnit.DAYS.between(now, endDate);
            } else {
                this.remainingDays = 0L;
            }
        }
    }
}