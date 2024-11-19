package com.henrytran1803.BEBakeManage.product.service;

import com.henrytran1803.BEBakeManage.Image.entity.Image;
import com.henrytran1803.BEBakeManage.Image.repository.ImageRepository;
import com.henrytran1803.BEBakeManage.category.entity.Category;
import com.henrytran1803.BEBakeManage.category.repository.CategoryRepository;
import com.henrytran1803.BEBakeManage.common.exception.error.ErrorCode;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.product.dto.*;
import com.henrytran1803.BEBakeManage.product.entity.*;
import com.henrytran1803.BEBakeManage.product.mapper.ProductMapper;
import com.henrytran1803.BEBakeManage.product.repository.*;
import com.henrytran1803.BEBakeManage.product.specification.ProductSpecification;
import com.henrytran1803.BEBakeManage.recipe.entity.Recipe;
import com.henrytran1803.BEBakeManage.recipe.repository.RecipeRepository;
import com.henrytran1803.BEBakeManage.user.entity.User;
import com.henrytran1803.BEBakeManage.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.sql.Timestamp;
import org.springframework.data.domain.Page;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final EntityManager entityManager;
    private final ProductRepository productRepository;
    private final ProductHistoryRepository productHistoryRepository;
    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final ImageRepository imageRepository;
    private final ProductBatchRepository productBatchRepository;
    private final UserRepository userRepository;
    private final DisposedProductDetailRepository disposedProductDetailRepository;
    private final DisposedProductRepository disposedProductRepository;
    private final ProductMapper productMapper;
    private final ProductDetailsRepository productDetailsRepository;
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
            Category category = categoryRepository.findById(createProductDTO.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + createProductDTO.getCategoryId()));
            product.setCategory(category);
            product.setName(createProductDTO.getName());
            product.setDescription(createProductDTO.getDescription());
            product.setStatus(true);
            product.setWeight(createProductDTO.getWeight());
            product.setLength(createProductDTO.getLength());
            product.setWidth(createProductDTO.getWidth());
            product.setHeight(createProductDTO.getHeight());
            product.setDiscountLimit(createProductDTO.getDiscountLimit());
            Recipe recipe = recipeRepository.findById(createProductDTO.getRecipeId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + createProductDTO.getRecipeId()));

            product.setRecipe(recipe);
            product.setCurrentPrice(createProductDTO.getPrice());
            product.setShelfLifeDays(createProductDTO.getShelfLifeDays());
            product.setShelfLifeDaysWarning(createProductDTO.getShelfLifeDaysWarning());

            Product savedProduct = productRepository.save(product);
            ProductHistory initialHistory = new ProductHistory();
            initialHistory.setProduct(savedProduct);
            initialHistory.setPrice(createProductDTO.getPrice());
            initialHistory.setEffectiveDate(LocalDateTime.now());
            productHistoryRepository.save(initialHistory);
            if (createProductDTO.getImageUrls() != null) {
                for (String url : createProductDTO.getImageUrls()) {
                    Image image = new Image();
                    image.setProduct(savedProduct);
                    image.setUrl(url);
                    imageRepository.save(image);
                }
            }

            createProductDTO.setCategoryId(savedProduct.getCategory().getId());
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

                        Category category = categoryRepository.findById(updateProductDTO.getCategoryId())
                                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + updateProductDTO.getCategoryId()));
                        existingProduct.setCategory(category);
                        existingProduct.setName(updateProductDTO.getName());
                        existingProduct.setDescription(updateProductDTO.getDescription());
                        existingProduct.setWeight(updateProductDTO.getWeight());
                        existingProduct.setLength(updateProductDTO.getLength());
                        existingProduct.setWidth(updateProductDTO.getWidth());
                        existingProduct.setHeight(updateProductDTO.getHeight());
                        existingProduct.setDiscountLimit(updateProductDTO.getDiscountLimit());
                        Recipe recipe = recipeRepository.findById(updateProductDTO.getRecipeId())
                                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + updateProductDTO.getRecipeId()));
                        existingProduct.setRecipe(recipe);
                        existingProduct.setShelfLifeDays(updateProductDTO.getShelfLifeDays());
                        existingProduct.setShelfLifeDaysWarning(updateProductDTO.getShelfLifeDaysWarning());

                        if (!existingProduct.getCurrentPrice().equals(updateProductDTO.getPrice())) {
                            ProductHistory priceHistory = new ProductHistory();
                            priceHistory.setProduct(existingProduct);
                            priceHistory.setPrice(updateProductDTO.getPrice());
                            priceHistory.setEffectiveDate(LocalDateTime.now());
                            productHistoryRepository.save(priceHistory);
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
            if (product.getStatus()) {
                product.setStatus(false);

            }else {
                product.setStatus(true);

            }
            productRepository.save(product);

            return null;
        } catch (Exception e) {
            throw new RuntimeException(ErrorCode.PRODUCT_DELETION_FAILED.getMessage() + ": " + e.getMessage());
        }
    }

    @Override
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);

        // Ánh xạ Page<Product> sang Page<ProductDTO>
        return products.map(product -> {
            ProductDTO dto = new ProductDTO();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setCurrentPrice(product.getCurrentPrice());
            dto.setStatus(product.getStatus());

            // Lấy tên danh mục nếu có
            if (product.getCategory() != null) {
                dto.setCategoryName(product.getCategory().getName());
            }

            // Lấy danh sách URL của ảnh cho sản phẩm
            List<Image> images = imageRepository.findByProductId(product.getId());
            List<String> imageUrls = images.stream()
                    .map(Image::getUrl)
                    .collect(Collectors.toList());
            dto.setImages(imageUrls); // Thêm danh sách URL ảnh vào DTO

            return dto;
        });
    }
    @Override
    public List<ProductActiveDTO> getAllActiveProducts() {
        List<ProductDetailProjection> projections = productRepository.findAllProductDetails();
        Map<Long, Product> productMap = new HashMap<>();

        for (ProductDetailProjection projection : projections) {
            Product product = productMap.computeIfAbsent(projection.getProductId(), id -> {
                Product p = new Product();
                p.setId(Math.toIntExact(id));
                p.setName(projection.getProductName());
                p.setDescription(projection.getProductDescription());
                p.setCurrentPrice(projection.getCurrentPrice());
                p.setWeight(projection.getWeight());
                p.setLength(projection.getLength());
                p.setWidth(projection.getWidth());
                p.setHeight(projection.getHeight());
                p.setDiscountLimit(projection.getDiscountLimit());
                p.setShelfLifeDays(projection.getShelfLifeDays());
                return p;
            });

            if (projection.getImageId() != null) {
                Image image = new Image();
                image.setId(projection.getImageId());
                image.setUrl(projection.getImageUrl());
                product.getImages().add(image);
            }

            if (projection.getBatchId() != null) {
                ProductBatch batch = new ProductBatch();
                batch.setId(projection.getBatchId());
                batch.setExpirationDate(projection.getBatchExpirationDate());
                batch.setCurrentDiscount(projection.getBatchCurrentDiscount());
                batch.setQuantity(projection.getBatchQuantity());
                batch.setStatus(projection.getBatchStatus());
                product.getProductBatches().add(batch);
            }
        }
        return new ArrayList<>(productMap.values().stream()
                .map(this::convertToProductDTO)
                .collect(Collectors.toList()));
    }

    public ApiResponse<Page<ProductDTO>> searchProducts(ProductSearchCriteria criteria) {
        try {
            Sort sort = Sort.by(
                    criteria.getSortDir().equalsIgnoreCase("asc")
                            ? Sort.Direction.ASC
                            : Sort.Direction.DESC,
                    criteria.getSortBy()
            );

            PageRequest pageRequest = PageRequest.of(
                    criteria.getPage(),
                    criteria.getSize(),
                    sort
            );

            Page<ProductDTO> productDTOs = productRepository
                    .findAll(ProductSpecification.getSpecification(criteria), pageRequest)
                    .map(productMapper::toDTO);

            return ApiResponse.success(productDTOs);
        } catch (Exception e) {
            return ApiResponse.error("SEARCH_ERROR", "Lỗi khi tìm kiếm sản phẩm: " + e.getMessage());
        }
    }
    @Transactional(readOnly = true)
    public ApiResponse<ProductDetailDTO> getProductDetail(Integer id) {
        try {
            return productRepository.findById(id)
                    .map(product -> {
                        Hibernate.initialize(product.getImages());
                        if (product.getCategory() != null) {
                            Hibernate.initialize(product.getCategory());
                        }
                        if (product.getRecipe() != null) {
                            Hibernate.initialize(product.getRecipe());
                        }
                        return ApiResponse.success(productMapper.toDetailDTO(product));
                    })
                    .orElse(ApiResponse.error("PRODUCT_NOT_FOUND",
                            "Không tìm thấy sản phẩm với ID: " + id));
        } catch (Exception e) {
            return ApiResponse.error("ERROR",
                    "Lỗi khi lấy thông tin chi tiết sản phẩm: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<List<ProductSummaryDTO>> getListProductBatch() {
        List<Product> products = productRepository.findAll();
        List<String> statuses = Arrays.asList("active", "near_expiry");
        List<ProductSummaryDTO> productSummaryDTOList = new ArrayList<>();
        for (Product product : products) {
            List<ProductBatch> productBatches = productBatchRepository.findByProductIdAndStatusIn(product.getId(), statuses);
            List<ProductBatchSumaryDTO> productBatchDTOs = productBatches.stream().map(batch -> {
                ProductBatchSumaryDTO dto = new ProductBatchSumaryDTO();
                dto.setId((long) batch.getId());
                dto.setStatus(batch.getStatus());
                dto.setQuantity(batch.getQuantity());
                dto.setCurrentDiscount(batch.getCurrentDiscount());
                dto.setDailyDiscount(batch.getDailyDiscount());
                dto.setDateExpiry(batch.getExpirationDate().toLocalDate());
                dto.setCountDown((int) ChronoUnit.DAYS.between(LocalDate.now(), batch.getExpirationDate().toLocalDate()));
                return dto;
            }).collect(Collectors.toList());
            ProductSummaryDTO summaryDTO = new ProductSummaryDTO();
            summaryDTO.setId((long) product.getId());
            summaryDTO.setName(product.getName());
            summaryDTO.setShelfLifeDays(product.getShelfLifeDays());
            summaryDTO.setTotalProductBatch(productBatches.size());
            summaryDTO.setTotalNearExpiry(
                    (int) productBatches.stream()
                            .filter(batch -> "NEAR_EXPIRY".equalsIgnoreCase(batch.getStatus()))
                            .count()
            );
            summaryDTO.setTotalExpiry(
                    (int) productBatches.stream()
                            .filter(batch -> "EXPIRED".equalsIgnoreCase(batch.getStatus()))
                            .count()
            );
            summaryDTO.setProductBatches(productBatchDTOs);
            productSummaryDTOList.add(summaryDTO);
        }
        return ApiResponse.success(productSummaryDTOList);
    }
    @Override
    public ApiResponse<List<ProductBatchDetailDTO>> getListProductBatchByStatues(List<String> statuses) {
        List<Object[]> results = productBatchRepository.findProductBatchDetailsByStatuses(statuses);

        // Map the results to ProductBatchDetailDTO
        List<ProductBatchDetailDTO> dtos = results.stream()
                .map(result -> {
                    ProductBatchDetailDTO dto = new ProductBatchDetailDTO();

                    // Set values for the DTO
                    dto.setId(((Number) result[0]).longValue());
                    dto.setName((String) result[1]);
                    dto.setStatus((String) result[2]);
                    dto.setQuantity(((Number) result[3]).intValue());

                    // Set the dateExpiry based on the type of the result
                    if (result[4] instanceof java.sql.Timestamp) {
                        dto.setDateExpiry(((java.sql.Timestamp) result[4]).toLocalDateTime().toLocalDate());
                    } else if (result[4] instanceof java.sql.Date) {
                        dto.setDateExpiry(((java.sql.Date) result[4]).toLocalDate());
                    }

                    // Set countDown value
                    dto.setCountDown(((Number) result[5]).intValue());

                    Integer currentDiscount = productBatchRepository.findCurrentDiscountByProductBatchId(dto.getId());
                    dto.setCurrentDiscount(currentDiscount != null ? currentDiscount : 0);

                    Integer dailyDiscount = productBatchRepository.findDailyDiscountByProductBatchId(dto.getId());
                    dto.setDailyDiscount(dailyDiscount != null ? dailyDiscount : 0);

                    return dto;
                })
                .collect(Collectors.toList());

        return ApiResponse.success(dtos);
    }

    @Override
    public Boolean disposedProduct(DisposedProductDTO disposedProductDTO) {
        DisposedProduct disposedProduct = new DisposedProduct();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int id = Integer.parseInt((String) authentication.getPrincipal());
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        disposedProduct.setStaff(user.get());
        disposedProduct.setDateDisposed(LocalDateTime.now());
        disposedProduct.setNote(disposedProductDTO.getNote());

        disposedProduct = disposedProductRepository.save(disposedProduct);

        for (Integer productBatchId : disposedProductDTO.getProductBatchIds()) {
            Optional<ProductBatch> productBatchOpt = productBatchRepository.findById(productBatchId);

            if (productBatchOpt.isEmpty()) {
                throw new IllegalArgumentException("Product batch not found for ID: " + productBatchId);
            }

            ProductBatch productBatch = productBatchOpt.get();
            if (productBatch.getQuantity() < 0 || "DISPOSED".equals(productBatch.getStatus())) {
                throw new IllegalArgumentException(
                        "Product batch is already disposed or has no quantity remaining: ID " + productBatchId
                );
            }
            DisposedProductDetail disposedProductDetail = new DisposedProductDetail();
            disposedProductDetail.setDisposedProduct(disposedProduct);
            disposedProductDetail.setProductBatch(productBatch);
            disposedProductDetail.setDisposedQuantity(productBatch.getQuantity());
            disposedProductDetailRepository.save(disposedProductDetail);
            productBatch.setQuantity(0);
            productBatch.setStatus("DISPOSED");
            productBatchRepository.save(productBatch);
        }

        return true;
    }



    public ProductActiveDTO convertToProductDTO(Product product) {
        ProductActiveDTO dto = new ProductActiveDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setCurrentPrice(product.getCurrentPrice());
        dto.setDescription(product.getDescription());
        dto.setShelfLifeDays(product.getShelfLifeDays());
        dto.setWeight(product.getWeight());
        dto.setLength(product.getLength());
        dto.setWidth(product.getWidth());
        dto.setHeight(product.getHeight());
//dto.setShelfLifeDays();
        if (product.getCategory() != null) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(product.getCategory().getId());
            categoryDTO.setName(product.getCategory().getName());
            dto.setCategory(categoryDTO);
        }

        List<ImageDTO> imageDTOs = product.getImages().stream().map(image -> {
            ImageDTO imageDTO = new ImageDTO();
            imageDTO.setId(image.getId());
            imageDTO.setUrl(image.getUrl());
            return imageDTO;
        }).collect(Collectors.toList());
        dto.setImages(imageDTOs);

        List<ProductBatchDTO> batchDTOs = product.getProductBatches().stream().map(batch -> {
            ProductBatchDTO batchDTO = new ProductBatchDTO();
            batchDTO.setId(batch.getId());
            batchDTO.setExpirationDate(batch.getExpirationDate().toString());
            batchDTO.setCurrentDiscount(Double.valueOf(batch.getCurrentDiscount()));
            batchDTO.setStatus(batch.getStatus());
            batchDTO.setQuantity(batch.getQuantity());
            return batchDTO;
        }).collect(Collectors.toList());
        dto.setProductBatches(batchDTOs);

        return dto;
    }
}