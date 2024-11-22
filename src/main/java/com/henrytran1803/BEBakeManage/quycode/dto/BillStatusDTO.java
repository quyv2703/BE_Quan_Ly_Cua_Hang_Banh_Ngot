package com.henrytran1803.BEBakeManage.quycode.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BillStatusDTO {
    private Long billId;
    private String billStatus;
}