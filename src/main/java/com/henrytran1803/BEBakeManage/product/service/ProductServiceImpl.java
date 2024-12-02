package com.henrytran1803.BEBakeManage.product.service;

import com.henrytran1803.BEBakeManage.Image.entity.Image;
import com.henrytran1803.BEBakeManage.Image.repository.ImageRepository;
import com.henrytran1803.BEBakeManage.category.entity.Category;
import com.henrytran1803.BEBakeManage.category.repository.CategoryRepository;
import com.henrytran1803.BEBakeManage.common.exception.error.ErrorCode;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.disposed_product.dto.DisposedProductDTO;
import com.henrytran1803.BEBakeManage.disposed_product.entity.DisposedProduct;
import com.henrytran1803.BEBakeManage.disposed_product.entity.DisposedProductDetail;
import com.henrytran1803.BEBakeManage.disposed_product.repository.DisposedProductDetailRepository;
import com.henrytran1803.BEBakeManage.disposed_product.repository.DisposedProductRepository;
import com.henrytran1803.BEBakeManage.product.dto.*;
import com.henrytran1803.BEBakeManage.product.entity.*;
import com.henrytran1803.BEBakeManage.product.mapper.ProductMapper;
import com.henrytran1803.BEBakeManage.product.repository.*;
import com.henrytran1803.BEBakeManage.product.specification.ProductSpecification;
import com.henrytran1803.BEBakeManage.product_batches.dto.*;
import com.henrytran1803.BEBakeManage.product_batches.entity.ProductBatch;
import com.henrytran1803.BEBakeManage.product_batches.repository.ProductBatchRepository;
import com.henrytran1803.BEBakeManage.product_history.entity.ProductHistory;
import com.henrytran1803.BEBakeManage.product_history.repository.ProductHistoryRepository;
import com.henrytran1803.BEBakeManage.promotion.entity.Promotion;
import com.henrytran1803.BEBakeManage.promotion.entity.PromotionDetail;
import com.henrytran1803.BEBakeManage.promotion.repository.PromotionRepository;
import com.henrytran1803.BEBakeManage.recipe.entity.Recipe;
import com.henrytran1803.BEBakeManage.recipe.repository.RecipeRepository;
import com.henrytran1803.BEBakeManage.user.entity.User;
import com.henrytran1803.BEBakeManage.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private final PromotionRepository promotionRepository;




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
            batchDTO.setStatus(batch.getStatus());
            batchDTO.setQuantity(batch.getQuantity());
            return batchDTO;
        }).collect(Collectors.toList());
        dto.setProductBatches(batchDTOs);

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SearchProductResponse> searchProducts(
            String productName,
            List<Integer> categoryIds,
            Pageable pageable
    ) {
        // 1. Tìm products theo điều kiện
        Page<Product> productsPage;

        if (categoryIds != null && !categoryIds.isEmpty()) {
            if (productName != null && !productName.trim().isEmpty()) {
                productsPage = productRepository.findByCategoryIdInAndNameContainingIgnoreCaseAndStatusIsTrue(
                        categoryIds, productName, pageable);
            } else {
                productsPage = productRepository.findByCategoryIdInAndStatusIsTrue(categoryIds, pageable);
            }
        } else {
            if (productName != null && !productName.trim().isEmpty()) {
                productsPage = productRepository.findByNameContainingIgnoreCaseAndStatusIsTrue(productName, pageable);
            } else {
                productsPage = productRepository.findByStatusIsTrue(pageable);
            }
        }

        return productsPage.map(product -> {
            // Lấy URL ảnh đầu tiên
            String imageUrl = product.getImages().stream()
                    .findFirst()
                    .map(Image::getUrl)
                    .orElse(null);

            // Lấy thông tin từ product batches
            List<ProductBatch> activeBatches = productBatchRepository.findByProductIdAndStatusIn(
                    product.getId(),
                    Arrays.asList("ACTIVE", "NEAR_EXPIRY")
            );

            // Tính max discount và total quantity
            Integer maxDiscount = activeBatches.stream()
                    .map(ProductBatch::getDailyDiscount)
                    .filter(discount -> discount != null)
                    .max(Integer::compareTo)
                    .orElse(0);

            Integer totalQuantity = activeBatches.stream()
                    .map(ProductBatch::getQuantity)
                    .filter(quantity -> quantity != null)
                    .reduce(0, Integer::sum);

            return new SearchProductResponse(
                    product.getId(),
                    product.getName(),
                    imageUrl,
                    product.getCategory() != null ? product.getCategory().getName() : null,
                    product.getCategory() != null ? product.getCategory().getId() : null,
                    maxDiscount,
                    product.getCurrentPrice(),
                    totalQuantity
            );
        });
    }
    @Override
    @Transactional(readOnly = true)
    public ProductDetailResponse getProductDetailForUser(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElse(null);

        return convertToDetailResponse(product);
    }

    private ProductDetailResponse convertToDetailResponse(Product product) {
        ProductDetailResponse response = new ProductDetailResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setCurrentPrice(product.getCurrentPrice());

        // Set category info if exists
        if (product.getCategory() != null) {
            ProductDetailResponse.CategoryInfo categoryInfo = new ProductDetailResponse.CategoryInfo(
                    product.getCategory().getId(),
                    product.getCategory().getName()
            );
            response.setCategory(categoryInfo);
        }

        // Convert all images to list
        List<String> imageUrls = product.getImages().stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());
        response.setImageUrls(imageUrls);  // Thay đổi từ setImageUrl sang setImageUrls

        // Convert product batches
        List<ProductDetailResponse.ProductBatchInfo> batchInfos = product.getProductBatches().stream()
                .filter(batch -> "ACTIVE".equals(batch.getStatus()) ||
                        "NEAR_EXPIRY".equals(batch.getStatus()))
                .map(batch -> new ProductDetailResponse.ProductBatchInfo(
                        batch.getId(),
                        batch.getExpirationDate(),
                        batch.getQuantity(),
                        batch.getStatus(),
                        batch.getDailyDiscount()
                ))
                .collect(Collectors.toList());
        response.setProductBatches(batchInfos);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartResponseDTO> getCartInfo(CartDTO cartDTO) {
        List<CartResponseDTO> cartResponseList = new ArrayList<>();
        Promotion validPromotion = null;
        if (cartDTO.getDiscountCode() != null && !cartDTO.getDiscountCode().isEmpty()) {
            validPromotion = getValidPromotion(cartDTO.getDiscountCode());
        }
        for (ProductBatchCart item : cartDTO.getProductBatchCarts()) {
            Optional<ProductBatch> batchOpt = productBatchRepository.findById(item.getProductBatchId());

            if (batchOpt.isPresent()) {
                ProductBatch batch = batchOpt.get();
                CartResponseDTO response = new CartResponseDTO();

                // Set basic product information
                response.setProductBatchId(batch.getId());
                response.setName(batch.getProduct().getName());
                response.setDailyDiscount(batch.getDailyDiscount() != null ? batch.getDailyDiscount() : 0);
                response.setCategoryName(batch.getProduct().getCategory().getName());
                response.setPrice(batch.getProduct().getCurrentPrice());

                // Set quantity information
                response.setQuantity(item.getQuantity());
                response.setQuantityRemain(batch.getQuantity() != null ? batch.getQuantity() : 0);

                // Get first image URL if available
                Optional<Image> firstImage = batch.getProduct().getImages().stream().findFirst();
                response.setImageUrl(firstImage.map(Image::getUrl).orElse(null));

                // Check if this batch is eligible for promotion discount
                int promotionDiscount = 0;
                if (validPromotion != null) {
                    promotionDiscount = getPromotionDiscountForBatch(validPromotion, batch);
                }
                response.setDiscountBonus(promotionDiscount);

                cartResponseList.add(response);
            }
        }

        return cartResponseList;
    }

    private Promotion getValidPromotion(String discountCode) {
        Optional<Promotion> promotionOpt = promotionRepository.findByName(discountCode);

        if (promotionOpt.isPresent()) {
            Promotion promotion = promotionOpt.get();
            LocalDateTime now = LocalDateTime.now();

            // Check if promotion is active and within valid date range
            if (promotion.getIsActive() &&
                    now.isAfter(promotion.getStartDate()) &&
                    now.isBefore(promotion.getEndDate())) {
                return promotion;
            }
        }
        return null;
    }

    private int getPromotionDiscountForBatch(Promotion promotion, ProductBatch batch) {
        // Check if this batch is included in the promotion
        for (PromotionDetail detail : promotion.getPromotionDetails()) {
            if (detail.getProductBatch().getId() == batch.getId()) {
                return promotion.getDiscount();
            }
        }
        return 0;
    }
}