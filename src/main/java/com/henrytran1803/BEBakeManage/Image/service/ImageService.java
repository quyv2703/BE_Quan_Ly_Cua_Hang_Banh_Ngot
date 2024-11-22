package com.henrytran1803.BEBakeManage.Image.service;

import com.henrytran1803.BEBakeManage.Image.dto.CreateImageDTO;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;

public interface ImageService {
    ApiResponse deleteProductImageById(int id);
    ApiResponse<?> createImage(CreateImageDTO createImageDTO);

}
