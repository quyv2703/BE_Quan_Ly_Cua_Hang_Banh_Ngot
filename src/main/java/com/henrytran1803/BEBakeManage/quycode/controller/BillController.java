package com.henrytran1803.BEBakeManage.quycode.controller;

import com.henrytran1803.BEBakeManage.common.exception.error.QuyExeption;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.quycode.BillStatus;
import com.henrytran1803.BEBakeManage.quycode.dto.BillStatusHistoryDTO;
import com.henrytran1803.BEBakeManage.quycode.entity.Bill;
import com.henrytran1803.BEBakeManage.quycode.request.BillRequest;
import com.henrytran1803.BEBakeManage.quycode.dto.BillStatusDTO;

import com.henrytran1803.BEBakeManage.quycode.response.BillResponse;
import com.henrytran1803.BEBakeManage.quycode.response.BillResponseNoDetail;
import com.henrytran1803.BEBakeManage.quycode.response.BillResponse_View_Cake;
import com.henrytran1803.BEBakeManage.quycode.service.BillService;
import com.henrytran1803.BEBakeManage.user.entity.User;
import com.henrytran1803.BEBakeManage.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/bills")
public class BillController {
    @Autowired
    private BillService billService;
    @Autowired
    UserRepository userRepository;


    // API tìm kiếm hóa đơn với phân trang
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<BillResponseNoDetail>>> searchBills(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "customerName", required = false) String customerName,
            @RequestParam(value = "customerPhone", required = false) String customerPhone,
            Pageable pageable) {
        ApiResponse<Page<BillResponseNoDetail>> response=billService.searchBills(id,customerName,customerPhone,pageable);
        return  ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
                .body(response);
    }

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
            @RequestParam BillStatus newStatus) {  // Đổi thành String
        try {
            ApiResponse<BillStatusDTO> response = billService.updateBillStatus(billId, newStatus);

            return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
                    .body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.Q_failure(null, QuyExeption.INVALID_STATUS)
            );
        }
    }

    // API lấy danh sách bills theo trạng thái
    @GetMapping("/status")
    public ApiResponse<Page<BillResponseNoDetail>> getBillsByStatus(
            @RequestParam BillStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Tạo đối tượng Pageable từ các tham số page và size
        Pageable pageable = PageRequest.of(page, size);

        // Gọi service và trả về kết quả phân trang
        return billService.getBillsByStatus(status, pageable);
    }


    // API lấy chi tiết bill theo ID
    @GetMapping("/{billId}")
    public ResponseEntity<ApiResponse<BillResponse_View_Cake>> getBillDetails(@PathVariable Long billId) {
        ApiResponse<BillResponse_View_Cake> response = billService.getBillDetailsById(billId);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response); // HTTP 200 nếu tìm thấy
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // HTTP 404 nếu không tìm thấy
        }
    }
}
