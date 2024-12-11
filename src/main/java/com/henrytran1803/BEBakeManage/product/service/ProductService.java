package com.henrytran1803.BEBakeManage.product.service;

import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.disposed_product.dto.DisposedProductDTO;
import com.henrytran1803.BEBakeManage.product.dto.*;
import com.henrytran1803.BEBakeManage.product_batches.dto.CartDTO;
import com.henrytran1803.BEBakeManage.product_batches.dto.CartResponseDTO;
import com.henrytran1803.BEBakeManage.product_batches.dto.ProductBatchDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {
CreateProductDTO createProduct(CreateProductDTO createProductDTO);
 Optional<UpdateProductDTO> updateProduct(UpdateProductDTO product);
 Void deleteProduct(int idProduct);
 Page<ProductDTO> getAllProducts(Pageable pageable);
 List<ProductActiveDTO> getAllActiveProducts();
 ApiResponse<Page<ProductDTO>> searchProducts(ProductSearchCriteria criteria);
 ApiResponse<ProductDetailDTO> getProductDetail(Integer id);


    Page<SearchProductResponse> searchProducts(
            String productName,
            List<Integer> categoryIds,
            Pageable pageable
    );
    ProductDetailResponse getProductDetailForUser(Integer productId);
    List<CartResponseDTO> getCartInfo(CartDTO cartDTO);
  }
                                      