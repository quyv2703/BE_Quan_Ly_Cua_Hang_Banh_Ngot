package com.henrytran1803.BEBakeManage.product.repository;

import com.henrytran1803.BEBakeManage.product.dto.ProductDetailProjection;

import java.util.List;

public interface ProductDetailsRepository {
    List<ProductDetailProjection> findAllActiveProducts();
}