package com.henrytran1803.BEBakeManage.quycode.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillResponseNoDetail {
    private Long billId;
    private String customerName;
    private String customerPhone;
    private String paymentMethod;
    private String diningOption;
    private String billStatus;
    private Double totalAmount;
    public BillResponseNoDetail() {}
}
