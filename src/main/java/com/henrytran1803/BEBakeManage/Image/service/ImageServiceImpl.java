package com.henrytran1803.BEBakeManage.Image.service;

import com.henrytran1803.BEBakeManage.Image.dto.CreateImageDTO;
import com.henrytran1803.BEBakeManage.Image.dto.ImageDTO;
import com.henrytran1803.BEBakeManage.Image.dto.ImageProductDTO;
import com.henrytran1803.BEBakeManage.Image.entity.Image;
import com.henrytran1803.BEBakeManage.Image.repository.ImageRepository;
import com.henrytran1803.BEBakeManage.common.exception.error.ErrorCode;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.product.entity.Product;
import com.henrytran1803.BEBakeManage.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageServiceImpl implements ImageService {
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    @Override
    public ApiResponse<?> deleteProductImageById(int id) {
        Optional<Image> image = imageRepository.findById(id);
        if (image.isPresent()) {
            imageRepository.delete(image.get());
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error(ErrorCode.IMAGE_NOT_FOUND.getCode(), "Image not found");
        }
    }

    @Override
    public ApiResponse<?> createImage(CreateImageDTO createImageDTO) {
        Optional<Product> productOptional = productRepository.findById(createImageDTO.getProductId());
        if (productOptional.isEmpty()) {
            return ApiResponse.error(ErrorCode.PRODUCT_NOT_FOUND.getCode(), "Product not found");
        }
        Image image = new Image();
        image.setProduct(productOptional.get());
        image.setUrl(createImageDTO.getUrl());

        imageRepository.save(image);

        // Chuyển đổi `Image` thành `ImageDTO`
        ImageProductDTO imageDTO = new ImageProductDTO(image.getId(), image.getUrl(), image.getProduct().getId());
        return ApiResponse.success(imageDTO);
    }

}
