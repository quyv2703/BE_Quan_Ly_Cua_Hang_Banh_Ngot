package com.henrytran1803.BEBakeManage.quycode.controller;

import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.quycode.dto.BillRequest;
import com.henrytran1803.BEBakeManage.quycode.entity.Bill;
import com.henrytran1803.BEBakeManage.quycode.repository.BillRepository;
import com.henrytran1803.BEBakeManage.quycode.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/bills")
public class BillController {
    @Autowired
    private BillService billService;

    @PostMapping
    public ApiResponse<Bill> createBill(@RequestBody BillRequest billRequest) {
        return billService.createBill(billRequest);
    }

}
