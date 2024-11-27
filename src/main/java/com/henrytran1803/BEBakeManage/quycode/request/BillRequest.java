package com.henrytran1803.BEBakeManage.quycode.request;

import com.henrytran1803.BEBakeManage.quycode.DiningOption;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class BillRequest {
    private String customerName;
    private String customerPhone;
    private String paymentMethod;
    private Long tableId;
    private DiningOption diningOption; // Thêm trường mới
    private List<BillDetailRequest> billDetails;
}
