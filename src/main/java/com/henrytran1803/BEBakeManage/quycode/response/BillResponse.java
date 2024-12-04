package com.henrytran1803.BEBakeManage.quycode.response;

import com.henrytran1803.BEBakeManage.quycode.DiningOption;
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
    private DiningOption diningOption; // Thêm trạng thái
    private Double totalAmount;
    private List<BillDetailDTO> billDetails;

    public BillResponse() {}

    // Getters và Setters
}
