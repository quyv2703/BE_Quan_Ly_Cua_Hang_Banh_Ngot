package com.henrytran1803.BEBakeManage.product_history.service;

import com.henrytran1803.BEBakeManage.product.dto.ProductHistoryDTO;

import java.util.List;

public interface ProductPriceService {
    void updateProductPrice(Integer productId, Double newPrice);
    Double getCurrentPrice(Integer productId);
    void updateAllProductsPrices();
    List<ProductHistoryDTO> getAllProductHistoryByIdProduct(int idProduct);
}
