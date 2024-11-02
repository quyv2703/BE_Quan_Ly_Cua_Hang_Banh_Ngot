package com.henrytran1803.BEBakeManage.promotion.dto;


import lombok.Data;

import java.util.Date;

@Data
public class UpdatePromotionDTO {
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private Double discount;
}