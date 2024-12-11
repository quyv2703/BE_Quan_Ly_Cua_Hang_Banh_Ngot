package com.henrytran1803.BEBakeManage.disposed_product.service;

import com.henrytran1803.BEBakeManage.disposed_product.dto.DisposedProductDTO;
import com.henrytran1803.BEBakeManage.disposed_product.dto.DisposedProductDetailResponseDTO;
import com.henrytran1803.BEBakeManage.disposed_product.dto.DisposedProductSummaryDTO;

import java.util.List;

public interface DisposedProductService {
    Boolean disposedProduct(DisposedProductDTO disposedProductDTO);
    List<DisposedProductSummaryDTO> getAllDisposedProducts();
    DisposedProductDetailResponseDTO getDisposedProductById(int id);
}
