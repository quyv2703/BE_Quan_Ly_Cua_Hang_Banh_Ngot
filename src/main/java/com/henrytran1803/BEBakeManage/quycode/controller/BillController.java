package com.henrytran1803.BEBakeManage.quycode.controller;

import com.henrytran1803.BEBakeManage.common.exception.error.QuyExeption;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.quycode.BillStatus;
import com.henrytran1803.BEBakeManage.quycode.dto.BillStatisticsDTO;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/user/bills")
public class BillController {
    @Autowired
    private BillService billService;
    @Autowired
    UserRepository userRepository;

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

    @PostMapping
    public ResponseEntity<ApiResponse<BillResponse>> createBill(@RequestBody BillRequest billRequest) {
        ApiResponse<BillResponse> response = billService.createBill(billRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // HTTP 201
    }

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

    @GetMapping("/status")
    public ApiResponse<Page<BillResponseNoDetail>> getBillsByStatus(
            @RequestParam BillStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return billService.getBillsByStatus(status, pageable);
    }

    @GetMapping("/{billId}")
    public ResponseEntity<ApiResponse<BillResponse_View_Cake>> getBillDetails(@PathVariable Long billId) {
        ApiResponse<BillResponse_View_Cake> response = billService.getBillDetailsById(billId);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    @GetMapping("/today")
    public ResponseEntity<ApiResponse<BillStatisticsDTO>> getTodayStatistics() {
        ApiResponse<BillStatisticsDTO> response = billService.getTodayStatistics();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/month")
    public ResponseEntity<ApiResponse<BillStatisticsDTO>> getCurrentMonthStatistics() {
        ApiResponse<BillStatisticsDTO> response = billService.getCurrentMonthStatistics();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/year")
    public ResponseEntity<ApiResponse<BillStatisticsDTO>> getCurrentYearStatistics() {
        ApiResponse<BillStatisticsDTO> response = billService.getCurrentYearStatistics();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/custom")
    public ResponseEntity<ApiResponse<BillStatisticsDTO>> getCustomRangeStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate
    ) {
        LocalDateTime startDateTime = fromDate.atStartOfDay();
        LocalDateTime endDateTime = toDate.atTime(23, 59, 59);

        ApiResponse<BillStatisticsDTO> response = billService.getStatistics(startDateTime, endDateTime);
        return ResponseEntity.ok(response);
    }
}
