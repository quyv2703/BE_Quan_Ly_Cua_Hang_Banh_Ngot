package com.henrytran1803.BEBakeManage.promotion.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PromotionSearchCriteria {
    private Integer id;
    private String name;
    private String description;
    private Integer discount;
    private Boolean isActive;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "id";
    private String sortDir = "asc";
}