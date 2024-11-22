package com.henrytran1803.BEBakeManage.quycode.response;

import com.henrytran1803.BEBakeManage.quycode.dto.BillDetailDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class BillResponse {
    private Long billId;
    private String customerName;
    private String customerPhone;
    private String paymentMethod;
    private String billStatus;
    private Double totalAmount;
    private List<BillDetailDTO> billDetails;

    public BillResponse() {}

    // Getters và Setters
}
