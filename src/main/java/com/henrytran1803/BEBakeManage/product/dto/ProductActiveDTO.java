package com.henrytran1803.BEBakeManage.product.dto;

import com.henrytran1803.BEBakeManage.product_batches.dto.ProductBatchDTO;
import lombok.Data;

import java.util.List;
@Data
public class ProductActiveDTO {
    private Integer id;
    private String name;
    private Double currentPrice;
    private String description;
    private Integer shelfLifeDays;
    private Double weight;
    private Double length;
    private Double width;
    private Double height;
    private CategoryDTO category;
    private List<ImageDTO> images;
    private List<ProductBatchDTO> productBatches;

}