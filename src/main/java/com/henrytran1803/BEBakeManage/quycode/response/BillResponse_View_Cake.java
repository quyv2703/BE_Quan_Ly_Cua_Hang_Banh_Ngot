package com.henrytran1803.BEBakeManage.quycode.response;

import com.henrytran1803.BEBakeManage.quycode.DiningOption;
import com.henrytran1803.BEBakeManage.quycode.dto.BillDetailDTO_ViewCake;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillResponse_View_Cake {
    private Long billId;
    private String customerName;
    private String customerPhone;
    private String paymentMethod;
    private String nameArea;
    private String nameTable;
    private String billStatus;
    private DiningOption diningOption; // Thêm trạng thái
    private Double totalAmount;
    private List<BillDetailDTO_ViewCake> billDetails;
}
