package com.henrytran1803.BEBakeManage.quycode.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillStatusDTO {
    private Long billId;
    private String oldStatus;
    private String newStatus;
    private String updatedBy; // Ai thực hiện cập nhật (User hoặc SYSTEM)
    private LocalDateTime updatedAt;

}