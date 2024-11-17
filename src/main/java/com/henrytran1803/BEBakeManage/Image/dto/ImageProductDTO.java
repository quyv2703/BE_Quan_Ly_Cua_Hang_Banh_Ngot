package com.henrytran1803.BEBakeManage.Image.dto;

import lombok.Data;

@Data
public class ImageProductDTO {
    private Integer id;
    private String url;
    private Integer productId;

    // Constructor, getter, setter
    public ImageProductDTO(Integer id, String url, Integer productId) {
        this.id = id;
        this.url = url;
        this.productId = productId;
    }
}
