package com.henrytran1803.BEBakeManage.quycode.controller;

import com.henrytran1803.BEBakeManage.common.exception.error.QuyExeption;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.quycode.BillStatus;
import com.henrytran1803.BEBakeManage.quycode.dto.BillRequest;
import com.henrytran1803.BEBakeManage.quycode.dto.BillStatusDTO;

import com.henrytran1803.BEBakeManage.quycode.response.BillResponse;
import com.henrytran1803.BEBakeManage.quycode.response.BillResponseNoDetail;
import com.henrytran1803.BEBakeManage.quycode.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/bills")
public class BillController {
    @Autowired
    private BillService billService;

    // API tạo mới bill
    @PostMapping
    public ResponseEntity<ApiResponse<BillResponse>> createBill(@RequestBody BillRequest billRequest) {
        ApiResponse<BillResponse> response = billService.createBill(billRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // HTTP 201
    }

    // API cập nhật trạng thái bill
    @PutMapping("/{billId}/status")
    public ResponseEntity<ApiResponse<BillStatusDTO>> updateBillStatus(
            @PathVariable Long billId,
            @RequestParam String newBillStatus) {
        ApiResponse<BillStatusDTO> response = billService.updateBillStatus(billId, BillStatus.valueOf(newBillStatus));
        if (response.isSuccess()) {
            return ResponseEntity.ok(response); // HTTP 200 nếu cập nhật thành công
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // HTTP 400 nếu không hợp lệ
        }
    }

    // API lấy danh sách bills theo trạng thái
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<List<BillResponseNoDetail>>> getBillsByStatus(
            @RequestParam("status") String status) {
        try {
            BillStatus billStatus = BillStatus.valueOf(status.toUpperCase());
            ApiResponse<List<BillResponseNoDetail>> response = ApiResponse.Q_success(
                    billService.getBillsByStatus(billStatus),
                    QuyExeption.SUCCESS
            );
            return ResponseEntity.ok(response); // HTTP 200
        } catch (IllegalArgumentException e) {
            ApiResponse<List<BillResponseNoDetail>> response = ApiResponse.Q_failure(
                    null,
                    QuyExeption.INVALID_BILL_STATUS
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // HTTP 400
        }
    }


    // API lấy chi tiết bill theo ID
    @GetMapping("/{billId}")
    public ResponseEntity<ApiResponse<BillResponse>> getBillDetails(@PathVariable Long billId) {
        ApiResponse<BillResponse> response = billService.getBillDetailsById(billId);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response); // HTTP 200 nếu tìm thấy
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // HTTP 404 nếu không tìm thấy
        }
    }
}
