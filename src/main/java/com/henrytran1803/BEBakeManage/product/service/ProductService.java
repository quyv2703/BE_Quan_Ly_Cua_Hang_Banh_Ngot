package com.henrytran1803.BEBakeManage.product.service;

import com.henrytran1803.BEBakeManage.product.dto.CreateProductDTO;
import com.henrytran1803.BEBakeManage.product.dto.ProductDTO;
import com.henrytran1803.BEBakeManage.product.dto.UpdateProductDTO;
import com.henrytran1803.BEBakeManage.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<CreateProductDTO> createProduct(CreateProductDTO createProductDTO);
    Optional<UpdateProductDTO> updateProduct(UpdateProductDTO product);
    Void deleteProduct(int idProduct);
    Page<ProductDTO> getAllProducts(Pageable pageable);
    List<ProductDTO> getAllActiveProducts();

}
