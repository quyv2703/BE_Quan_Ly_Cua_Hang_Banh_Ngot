package com.henrytran1803.BEBakeManage.product.service;

import com.henrytran1803.BEBakeManage.category.repository.CategoryRepository;
import com.henrytran1803.BEBakeManage.common.exception.error.ErrorCode;
import com.henrytran1803.BEBakeManage.product.dto.CreateProductDTO;
import com.henrytran1803.BEBakeManage.product.dto.UpdateProductDTO;
import com.henrytran1803.BEBakeManage.product.entity.Product;
import com.henrytran1803.BEBakeManage.product.entity.ProductHistory;
import com.henrytran1803.BEBakeManage.product.repository.ProductHistoryRepository;
import com.henrytran1803.BEBakeManage.product.repository.ProductRepository;
import com.henrytran1803.BEBakeManage.product.service.ProductService;
import com.henrytran1803.BEBakeManage.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductHistoryRepository productHistoryRepository;
    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;

    @Override
    @Transactional
    public Optional<CreateProductDTO> createProduct(CreateProductDTO createProductDTO) {
        try {
            if (productRepository.existsByName(createProductDTO.getName())) {
                throw new RuntimeException(ErrorCode.DUPLICATE_PRODUCT_NAME.getMessage());
            }
            if (createProductDTO.getCategoryId() != null &&
                    !categoryRepository.existsById(createProductDTO.getCategoryId())) {
                throw new RuntimeException(ErrorCode.INVALID_CATEGORY.getMessage());
            }
            if (createProductDTO.getRecipeId() == null ||
                    !recipeRepository.existsById(createProductDTO.getRecipeId())) {
                throw new RuntimeException(ErrorCode.INVALID_RECIPE.getMessage());
            }
            Product product = new Product();
            product.setCategoryId(createProductDTO.getCategoryId());
            product.setName(createProductDTO.getName());
            product.setDescription(createProductDTO.getDescription());
            product.setStatus(true); // Set default status to true
            product.setWeight(createProductDTO.getWeight());
            product.setLength(createProductDTO.getLength());
            product.setWidth(createProductDTO.getWidth());
            product.setHeight(createProductDTO.getHeight());
            product.setDiscountLimit(createProductDTO.getDiscountLimit());
            product.setRecipeId(createProductDTO.getRecipeId());
            product.setCurrentPrice(createProductDTO.getPrice());

            Product savedProduct = productRepository.save(product);

            ProductHistory initialHistory = new ProductHistory();
            initialHistory.setProduct(savedProduct);
            initialHistory.setPrice(createProductDTO.getPrice());
            initialHistory.setEffectiveDate(LocalDateTime.now());
            productHistoryRepository.save(initialHistory);

            createProductDTO.setCategoryId(savedProduct.getCategoryId());
            return Optional.of(createProductDTO);

        } catch (Exception e) {
            throw new RuntimeException(ErrorCode.PRODUCT_CREATION_FAILED.getMessage() + ": " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Optional<UpdateProductDTO> updateProduct(UpdateProductDTO updateProductDTO) {
        try {
            return productRepository.findById(updateProductDTO.getId())
                    .map(existingProduct -> {
                        // Kiểm tra và cập nhật thông tin sản phẩm
                        if (!existingProduct.getName().equals(updateProductDTO.getName()) &&
                                productRepository.existsByName(updateProductDTO.getName())) {
                            throw new RuntimeException(ErrorCode.DUPLICATE_PRODUCT_NAME.getMessage());
                        }

                        if (updateProductDTO.getCategoryId() != null &&
                                !categoryRepository.existsById(updateProductDTO.getCategoryId())) {
                            throw new RuntimeException(ErrorCode.INVALID_CATEGORY.getMessage());
                        }

                        if (updateProductDTO.getRecipeId() == null ||
                                !recipeRepository.existsById(updateProductDTO.getRecipeId())) {
                            throw new RuntimeException(ErrorCode.INVALID_RECIPE.getMessage());
                        }

                        // Cập nhật các thuộc tính của sản phẩm
                        existingProduct.setCategoryId(updateProductDTO.getCategoryId());
                        existingProduct.setName(updateProductDTO.getName());
                        existingProduct.setDescription(updateProductDTO.getDescription());
                        existingProduct.setWeight(updateProductDTO.getWeight());
                        existingProduct.setLength(updateProductDTO.getLength());
                        existingProduct.setWidth(updateProductDTO.getWidth());
                        existingProduct.setHeight(updateProductDTO.getHeight());
                        existingProduct.setDiscountLimit(updateProductDTO.getDiscountLimit());
                        existingProduct.setRecipeId(updateProductDTO.getRecipeId());

                        // Kiểm tra nếu giá thay đổi
                        if (!existingProduct.getCurrentPrice().equals(updateProductDTO.getPrice())) {
                            // Tạo bản ghi lịch sử giá mới
                            ProductHistory priceHistory = new ProductHistory();
                            priceHistory.setProduct(existingProduct);
                            priceHistory.setPrice(updateProductDTO.getPrice());
                            priceHistory.setEffectiveDate(LocalDateTime.now());
                            productHistoryRepository.save(priceHistory);

                            // Cập nhật giá hiện tại cho sản phẩm
                            existingProduct.setCurrentPrice(updateProductDTO.getPrice());
                        }

                        productRepository.save(existingProduct);
                        return updateProductDTO;
                    });
        } catch (Exception e) {
            throw new RuntimeException(ErrorCode.PRODUCT_UPDATE_FAILED.getMessage() + ": " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public Void deleteProduct(int idProduct) {
        try {
            Product product = productRepository.findById(idProduct)
                    .orElseThrow(() -> new RuntimeException(ErrorCode.PRODUCT_NOT_FOUND.getMessage()));
            product.setStatus(false);
            productRepository.save(product);

            return null;
        } catch (Exception e) {
            throw new RuntimeException(ErrorCode.PRODUCT_DELETION_FAILED.getMessage() + ": " + e.getMessage());
        }
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);

    }


    @Override
    public List<Product> getAllActiveProducts() {
        return productRepository.findByStatusTrue();
    }
}