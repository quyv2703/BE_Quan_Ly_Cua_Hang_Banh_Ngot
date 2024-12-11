package com.henrytran1803.BEBakeManage.disposed_product.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DisposedProductDetailResponseDTO {
    private int id;
    private LocalDateTime dateDisposed;
    private String note;
    private String staffName;
    private List<DisposedBatchDTO> disposedBatches;
}
