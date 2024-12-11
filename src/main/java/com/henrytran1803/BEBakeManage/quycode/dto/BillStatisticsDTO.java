package com.henrytran1803.BEBakeManage.quycode.dto;

import com.henrytran1803.BEBakeManage.quycode.response.BillResponseNoDetail;
import lombok.Data;

import java.util.List;

@Data
public class BillStatisticsDTO {
    private List<BillResponseNoDetail> bills;
    private Double totalRevenue;
}