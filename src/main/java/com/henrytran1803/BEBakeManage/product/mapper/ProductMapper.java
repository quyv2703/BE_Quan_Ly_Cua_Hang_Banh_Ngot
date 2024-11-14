package com.henrytran1803.BEBakeManage.product.mapper;

import com.henrytran1803.BEBakeManage.product.dto.ProductDTO;
import com.henrytran1803.BEBakeManage.product.dto.ProductDetailDTO;
import com.henrytran1803.BEBakeManage.product.entity.Product;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductMapper {
    public ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setCurrentPrice(product.getCurrentPrice());
        dto.setCategoryName(product.getCategory() != null ? product.getCategory().getName() : null);
        dto.setStatus(product.getStatus());
        dto.setImages(product.getImages().stream()
                .map(image -> image.getUrl())
                .collect(Collectors.toList()));
        return dto;
    }


    // New mapper method for detail view
    public ProductDetailDTO toDetailDTO(Product product) {
        ProductDetailDTO dto = new ProductDetailDTO();

        // Map basic fields
        dto.setId(product.getId());
        dto.setCategoryId(product.getCategory().getId());
        dto.setName(product.getName());
        dto.setCurrentPrice(product.getCurrentPrice());
        dto.setDescription(product.getDescription());
        dto.setShelfLifeDays(product.getShelfLifeDays());
        dto.setStatus(product.getStatus());
        dto.setWeight(product.getWeight());
        dto.setLength(product.getLength());
        dto.setWidth(product.getWidth());
        dto.setHeight(product.getHeight());
        dto.setDiscountLimit(product.getDiscountLimit());
        dto.setRecipeId(product.getRecipeId());

        // Map Category info
        if (product.getCategory() != null) {
            ProductDetailDTO.CategoryInfo categoryInfo = new ProductDetailDTO.CategoryInfo();
            categoryInfo.setId(product.getCategory().getId());
            categoryInfo.setName(product.getCategory().getName());
            dto.setCategory(categoryInfo);
        }

        // Map Recipe info
        if (product.getRecipe() != null) {
            ProductDetailDTO.RecipeInfo recipeInfo = new ProductDetailDTO.RecipeInfo();
            recipeInfo.setId(product.getRecipe().getId());
            recipeInfo.setName(product.getRecipe().getName());
            dto.setRecipe(recipeInfo);
        }

        // Map Images
        dto.setImages(product.getImages().stream()
                .map(image -> {
                    ProductDetailDTO.ImageInfo imageInfo = new ProductDetailDTO.ImageInfo();
                    imageInfo.setId(image.getId());
                    imageInfo.setUrl(image.getUrl());
                    return imageInfo;
                })
                .collect(Collectors.toList()));

        return dto;
    }
}