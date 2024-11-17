package com.henrytran1803.BEBakeManage.Image.controller;

import com.henrytran1803.BEBakeManage.Image.dto.CreateImageDTO;
import com.henrytran1803.BEBakeManage.Image.dto.ImageDTO;
import com.henrytran1803.BEBakeManage.Image.service.ImageService;
import com.henrytran1803.BEBakeManage.common.exception.error.ErrorCode;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ImageUploadController {
    private final ImageService imageService;

    private static final String UPLOAD_DIR = "uploads/";
    @PostMapping("/upload")
    public ApiResponse<ImageDTO> uploadImage(@RequestParam("image") MultipartFile image, @RequestParam("productName") String productName) {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        int randomNum = new Random().nextInt(1000);
        String fileName = productName + "_" + randomNum + "_" + UUID.randomUUID().toString();
        try {
            Files.copy(image.getInputStream(), Paths.get(UPLOAD_DIR + fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.error(ErrorCode.IMAGE_UPLOAD_FAILED.getCode(),ErrorCode.IMAGE_UPLOAD_FAILED.getMessage());
        }
        return ApiResponse.success(new ImageDTO("/uploads/" + fileName));
    }
    @DeleteMapping("/upload")
    public ApiResponse deleteImage(@RequestParam("fileName") String fileName) {
        try {
            String processedFileName = fileName;

            if (fileName.startsWith("http://") || fileName.startsWith("https://")) {
                processedFileName = fileName.substring(fileName.lastIndexOf("/") + 1);
            }
            else if (fileName.startsWith("/upload/")) {
                processedFileName = fileName.substring("/uploads/".length());
            }
            else if (fileName.contains("/")) {
                processedFileName = fileName.substring(fileName.lastIndexOf("/") + 1);
            }

            File file = new File(UPLOAD_DIR + processedFileName);
            if (file.exists()) {
                Files.delete(Paths.get(UPLOAD_DIR + processedFileName));
                return ApiResponse.success("success");
            } else {
                return ApiResponse.error(ErrorCode.IMAGE_UPLOAD_FAILED.getCode(), ErrorCode.IMAGE_UPLOAD_FAILED.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(ErrorCode.IMAGE_UPLOAD_FAILED.getCode(), ErrorCode.IMAGE_UPLOAD_FAILED.getMessage());
        }
    }


    @DeleteMapping("/images/{id}")
    public ResponseEntity<ApiResponse<?>> deleteImage(@PathVariable int id) {
        try {
            ApiResponse<?> response = imageService.deleteProductImageById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ErrorCode.IMAGE_DELETION_FAILED.getCode(), e.getMessage()));
        }
    }
    @PostMapping("/images")
    public ResponseEntity<ApiResponse<?>> createImage(@Valid @RequestBody CreateImageDTO createImageDTO,
                                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ErrorCode.INVALID_IMAGE_DATA.getCode(),
                            bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }

        ApiResponse<?> response = imageService.createImage(createImageDTO);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
