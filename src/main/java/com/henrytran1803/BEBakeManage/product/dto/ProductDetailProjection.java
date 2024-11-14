package com.henrytran1803.BEBakeManage.product.dto;

import java.time.LocalDateTime;
public interface ProductDetailProjection {
    Long getProductId();
    String getProductName();
    String getProductDescription();
    Double getCurrentPrice();
    Double getWeight();
    Double getLength();
    Double getWidth();
    Double getHeight();
    Double getDiscountLimit();
    Integer getShelfLifeDays();
    Integer getImageId();
    String getImageUrl();
    Integer getBatchId();
    LocalDateTime getBatchExpirationDate();
    Integer getBatchCurrentDiscount();
    Integer getBatchQuantity();
    String getBatchStatus();
}
