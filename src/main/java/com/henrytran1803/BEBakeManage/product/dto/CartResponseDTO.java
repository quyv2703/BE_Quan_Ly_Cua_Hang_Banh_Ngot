package com.henrytran1803.BEBakeManage.product.dto;

import lombok.Data;

@Data
public class CartResponseDTO{
    private int productBatchId;
    private int quantity;
    private int quantityRemain;
    private String name;
    private int dailyDiscount;
    private int discountBonus;
    private String categoryName;
    private double price;
    private String imageUrl;
}
