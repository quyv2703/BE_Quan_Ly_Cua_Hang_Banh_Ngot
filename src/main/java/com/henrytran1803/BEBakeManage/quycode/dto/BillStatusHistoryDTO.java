package com.henrytran1803.BEBakeManage.quycode.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillStatusHistoryDTO {
    private Long id;
    private Long billId;
    private String oldStatus;
    private String newStatus;
    private Long updatedById;
    private String updatedByName;
    private LocalDateTime updatedAt;
}
