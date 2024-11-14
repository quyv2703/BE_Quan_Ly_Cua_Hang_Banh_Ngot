package com.henrytran1803.BEBakeManage.product.dto;
import lombok.Data;

@Data
public class ProductSearchCriteria {
    private String name;
    private Integer categoryId;
    private Boolean status;
    private Double minPrice;
    private Double maxPrice;
    private String sortBy = "id";
    private String sortDir = "asc";
    private int page = 0;
    private int size = 10;
}