package com.henrytran1803.BEBakeManage.product.controller;

import com.henrytran1803.BEBakeManage.common.exception.error.ErrorCode;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.product.dto.CreateProductDTO;
import com.henrytran1803.BEBakeManage.product.dto.ProductDTO;
import com.henrytran1803.BEBakeManage.product.dto.ProductHistoryDTO;
import com.henrytran1803.BEBakeManage.product.dto.UpdateProductDTO;
import com.henrytran1803.BEBakeManage.product.entity.Product;
import com.henrytran1803.BEBakeManage.product.service.ProductPriceService;
import com.henrytran1803.BEBakeManage.product.service.ProductService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductPriceService productPriceService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDTO> productDTOs = productService.getAllProducts(pageable);

        return ResponseEntity.ok(ApiResponse.success(productDTOs));
    }
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getActiveProducts() {
        List<ProductDTO> activeProductDTOs = productService.getAllActiveProducts();
        return ResponseEntity.ok(ApiResponse.success(activeProductDTOs));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createProduct(@Valid @RequestBody CreateProductDTO createProductDTO,
                                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ErrorCode.INVALID_PRODUCT_DATA.getCode(),
                            bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }

        try {
            return ResponseEntity.ok(ApiResponse.success(productService.createProduct(createProductDTO)));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ErrorCode.PRODUCT_CREATION_FAILED.getCode(), e.getMessage()));
        }
    }
    @PutMapping
    public ResponseEntity<ApiResponse<?>> updateProduct(@Valid @RequestBody UpdateProductDTO updateProductDTO,
                                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ErrorCode.INVALID_PRODUCT_DATA.getCode(),
                            bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }

        try {
            return ResponseEntity.ok(ApiResponse.success(productService.updateProduct(updateProductDTO)));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ErrorCode.PRODUCT_CREATION_FAILED.getCode(), e.getMessage()));
        }
    }
    @PostMapping("/{id}/price")
    public ResponseEntity<ApiResponse<?>> updatePrice(
            @PathVariable("id") Integer productId,
            @RequestBody Map<String, Double> priceUpdate) {
        try {
            productPriceService.updateProductPrice(productId, priceUpdate.get("price"));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.RECIPE_UPDATE_FAILED.getCode(), e.getMessage()));
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteProduct(
            @PathVariable("id") Integer productId)
            {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.RECIPE_UPDATE_FAILED.getCode(), e.getMessage()));
        }
    }
    @Transactional
    @GetMapping("/{id}/price/history")
    public ResponseEntity<ApiResponse<List<ProductHistoryDTO>>> getPriceHistory(@PathVariable("id") Integer productId) {
        List<ProductHistoryDTO> history = productPriceService.getAllProductHistoryByIdProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(history));
    }
}