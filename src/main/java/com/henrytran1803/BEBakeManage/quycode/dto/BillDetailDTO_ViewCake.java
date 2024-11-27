package com.henrytran1803.BEBakeManage.quycode.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillDetailDTO_ViewCake {
    private Long id;
    private Long productBatchId;
    private String productName;
    private List<String> productImages; // Danh sách ảnh sản phẩm
    private Integer quantity;
    private Double price; // Giá cuối cùng
    private LocalDateTime expirationDate; // Ngày hết hạn
    private String batchStatus; // Trạng thái lô
    private Double dailyDiscount; // Giảm giá hàng ngày
    private Double promotionDiscount; // Giảm giá từ khuyến mãi

    public <R> BillDetailDTO_ViewCake(Long id, long id1, String name, R collect, Integer quantity, LocalDateTime expirationDate, String status) {
    }
}
