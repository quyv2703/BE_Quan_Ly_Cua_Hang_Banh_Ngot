package com.henrytran1803.BEBakeManage.quycode.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class BillRequest {

    private String customerName;
    private String customerPhone;
    private String paymentMethod;
 /*   private String billStatus;*/
    private Long tableId; // Thông tin bàn
    private List<BillDetailRequest> billDetails;

    // Getters and Setters
}
