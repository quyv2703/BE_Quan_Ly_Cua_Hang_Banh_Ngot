package com.henrytran1803.BEBakeManage.Image.controller;

import com.henrytran1803.BEBakeManage.Image.dto.ImageDTO;
import com.henrytran1803.BEBakeManage.common.exception.error.ErrorCode;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ImageUploadController {
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
            File file = new File(UPLOAD_DIR + fileName);
            if (file.exists()) {
                Files.delete(Paths.get(UPLOAD_DIR + fileName));
                return ApiResponse.success("success");
            } else {
                return  ApiResponse.error(ErrorCode.IMAGE_UPLOAD_FAILED.getCode(), ErrorCode.IMAGE_UPLOAD_FAILED.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  ApiResponse.error(ErrorCode.IMAGE_UPLOAD_FAILED.getCode(), ErrorCode.IMAGE_UPLOAD_FAILED.getMessage());
        }
    }
}
