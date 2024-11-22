package com.henrytran1803.BEBakeManage.product.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CartDTO {
    private String discountCode;
    private ProductBatchCart[] productBatchCarts;


}