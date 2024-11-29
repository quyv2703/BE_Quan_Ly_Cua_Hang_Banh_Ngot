package com.henrytran1803.BEBakeManage.product.controller;

/*import com.google.protobuf.Api;*/
import com.henrytran1803.BEBakeManage.common.exception.error.ErrorCode;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.product.dto.*;
import com.henrytran1803.BEBakeManage.product.entity.Product;
import com.henrytran1803.BEBakeManage.product.service.ProductPriceService;
import com.henrytran1803.BEBakeManage.product.service.ProductService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductPriceService productPriceService;
    @GetMapping("/search")
    public ApiResponse<Page<ProductDTO>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ProductSearchCriteria criteria = new ProductSearchCriteria();
        criteria.setName(name);
        criteria.setCategoryId(categoryId);
        criteria.setStatus(status);
        criteria.setMinPrice(minPrice);
        criteria.setMaxPrice(maxPrice);
        criteria.setSortBy(sortBy);
        criteria.setSortDir(sortDir);
        criteria.setPage(page);
        criteria.setSize(size);

        return productService.searchProducts(criteria);
    }
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDTO> productDTOs = productService.getAllProducts(pageable);

        return ResponseEntity.ok(ApiResponse.success(productDTOs));
    }
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<ProductActiveDTO>>> getActiveProducts() {
        return ResponseEntity.ok(ApiResponse.success(productService.getAllActiveProducts()));

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
    public ResponseEntity<ApiResponse<?>> deleteProduct(@PathVariable("id") Integer productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok(ApiResponse.success("sucess"));
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

    @GetMapping("/{id}/detail")
    public ApiResponse<ProductDetailDTO> getProductDetail(@PathVariable Integer id) {
        return productService.getProductDetail(id);
    }
    @GetMapping("/productbatches")
    public ApiResponse<List<ProductSummaryDTO>> getListProductBatch() {
        return productService.getListProductBatch();
    }
    @GetMapping("/productbatches/statuses")
    public ApiResponse<List<ProductBatchDetailDTO>> getListProductBatchByStatus(@RequestParam List<String> statuses) {
        return productService.getListProductBatchByStatues(statuses);
    }
    @PostMapping("/productbatches")
    public ResponseEntity<ApiResponse<String>>getListProductBatchByStatus(@RequestBody DisposedProductDTO disposedProductDTO) {
        if (productService.disposedProduct(disposedProductDTO)){
            return ResponseEntity.ok().body(ApiResponse.success("Success"));
        }

        return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.RECIPE_UPDATE_FAILED.getCode(), ErrorCode.RECIPE_UPDATE_FAILED.getMessage()));

    }
    @GetMapping("/search/active")
    public ResponseEntity<ApiResponse<Page<SearchProductResponse>>> searchProducts(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) List<Integer> categoryIds, // Changed to List
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<SearchProductResponse> products = productService.searchProducts(
                productName,
                categoryIds,
                pageRequest
        );

        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getProductDetailForUser(@PathVariable Integer id) {
        ProductDetailResponse product = productService.getProductDetailForUser(id);
        return ResponseEntity.ok(ApiResponse.success(product));

    }

    @PostMapping("/cart")
    public ResponseEntity<ApiResponse< List<CartResponseDTO>>> getCartInfo(@RequestBody CartDTO cartDTO) {
        List<CartResponseDTO> product = productService.getCartInfo(cartDTO);
        return ResponseEntity.ok(ApiResponse.success(product));

    }


}