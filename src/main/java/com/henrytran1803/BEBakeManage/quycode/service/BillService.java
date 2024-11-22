package com.henrytran1803.BEBakeManage.quycode.service;

import com.henrytran1803.BEBakeManage.common.exception.error.QuyExeption;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.product.entity.ProductBatch;

import com.henrytran1803.BEBakeManage.product.repository.ProductBatchRepository;
import com.henrytran1803.BEBakeManage.quycode.BillStatus;
import com.henrytran1803.BEBakeManage.quycode.PaymentMethod;
import com.henrytran1803.BEBakeManage.quycode.dto.BillDetailDTO;
import com.henrytran1803.BEBakeManage.quycode.dto.BillDetailRequest;
import com.henrytran1803.BEBakeManage.quycode.dto.BillRequest;
import com.henrytran1803.BEBakeManage.quycode.dto.BillStatusDTO;
import com.henrytran1803.BEBakeManage.quycode.entity.Bill;
import com.henrytran1803.BEBakeManage.quycode.entity.BillDetail;
import com.henrytran1803.BEBakeManage.quycode.entity.Table;
import com.henrytran1803.BEBakeManage.quycode.repository.BillRepository;
import com.henrytran1803.BEBakeManage.quycode.repository.TableRepository;
import com.henrytran1803.BEBakeManage.quycode.response.BillResponse;
import com.henrytran1803.BEBakeManage.quycode.response.BillResponseNoDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private ProductBatchRepository productBatchRepository;


    //danh sach bill
    public List<BillResponseNoDetail> getBillsByStatus(BillStatus status) {
        List<Bill> bills = billRepository.findByBillStatus(status);
        return bills.stream().map(bill -> {
            BillResponseNoDetail responseNoDetail = new BillResponseNoDetail();
            responseNoDetail.setBillId(bill.getId());
            responseNoDetail.setCustomerName(bill.getCustomerName());
            responseNoDetail.setCustomerPhone(bill.getCustomerPhone());
            responseNoDetail.setPaymentMethod(bill.getPaymentMethod().name());
            responseNoDetail.setBillStatus(bill.getBillStatus().name());
            responseNoDetail.setTotalAmount(bill.getTotalAmount());
            return responseNoDetail;
        }).collect(Collectors.toList());
    }


    public ApiResponse<BillResponse> getBillDetailsById(Long billId) {
        Optional<Bill> billOptional = billRepository.findById(billId);
        if (billOptional.isEmpty()) {
            return ApiResponse.Q_failure(null, QuyExeption.BILL_NOT_FOUND);
        }
        Bill bill = billOptional.get();

        BillResponse response = new BillResponse();
        response.setBillId(bill.getId());
        response.setCustomerName(bill.getCustomerName());
        response.setCustomerPhone(bill.getCustomerPhone());
        response.setPaymentMethod(bill.getPaymentMethod().name());
        response.setBillStatus(bill.getBillStatus().name());
        response.setTotalAmount(bill.getTotalAmount());
        response.setBillDetails(bill.getBillDetails().stream().map(detail -> new BillDetailDTO(
                detail.getId(),
                (long) detail.getProductBatch().getId(),
                detail.getQuantity(),
                detail.getPrice()
        )).collect(Collectors.toList()));

        return ApiResponse.Q_success(response, QuyExeption.SUCCESS);
    }
    //tạo bill
    public ApiResponse<BillResponse> createBill(BillRequest billRequest) {
        Bill bill = new Bill();
        bill.setCustomerName(billRequest.getCustomerName());
        bill.setCustomerPhone(billRequest.getCustomerPhone());
        bill.setPaymentMethod(PaymentMethod.valueOf(billRequest.getPaymentMethod()));
        bill.setBillStatus(BillStatus.NOT_PAID);

        // Gán thông tin bàn nếu có
        if (billRequest.getTableId() != null) {
            Optional<Table> tableOptional = tableRepository.findById(billRequest.getTableId());
            if (tableOptional.isEmpty()) {
                return ApiResponse.Q_failure(null, QuyExeption.TABLE_NOT_FOUND);
            }
            bill.setTable(tableOptional.get());
        }

        // Xử lý danh sách chi tiết hóa đơn
        for (BillDetailRequest detailRequest : billRequest.getBillDetails()) {
            BillDetail billDetail = new BillDetail();
            Optional<ProductBatch> productBatchOptional = productBatchRepository.findById(Math.toIntExact(detailRequest.getProductBatchId()));
            if (productBatchOptional.isEmpty()) {
                return ApiResponse.Q_failure(null, QuyExeption.PRODUCT_BATCH_NOT_FOUND);
            }
            billDetail.setProductBatch(productBatchOptional.get());
            billDetail.setQuantity(detailRequest.getQuantity());
            billDetail.setPrice(detailRequest.getPrice());

            // Kiểm tra trùng lặp trước khi thêm
            if (bill.getBillDetails().stream()
                    .anyMatch(detail -> detail.getProductBatch().getId() == billDetail.getProductBatch().getId())) {
                continue; // Bỏ qua nếu đã tồn tại
            }

            bill.addBillDetail(billDetail);
        }

        billRepository.save(bill);

        // Chuyển đổi Bill sang DTO
        BillResponse billResponse = new BillResponse();
        billResponse.setBillId(bill.getId());
        billResponse.setCustomerName(bill.getCustomerName());
        billResponse.setCustomerPhone(bill.getCustomerPhone());
        billResponse.setPaymentMethod(bill.getPaymentMethod().name());
        billResponse.setBillStatus(bill.getBillStatus().name());
        billResponse.setTotalAmount(bill.getTotalAmount());
        billResponse.setBillDetails(getBillDetails(bill)); // Chuyển đổi danh sách chi tiết

        return ApiResponse.Q_success(billResponse, QuyExeption.SUCCESS);
    }

    public ApiResponse<BillStatusDTO> updateBillStatus(Long billId, BillStatus newBillStatus) {
        // Tìm hóa đơn theo ID
        Optional<Bill> optionalBill = billRepository.findById(billId);
        if (optionalBill.isEmpty()) {
            return ApiResponse.Q_failure(null, QuyExeption.BILL_NOT_FOUND);
        }

        // Lấy hóa đơn và cập nhật trạng thái
        Bill bill = optionalBill.get();
        bill.setBillStatus(BillStatus.valueOf(String.valueOf(newBillStatus))); // Cập nhật trạng thái
        billRepository.save(bill); // Lưu lại thay đổi

        // Trả về DTO chỉ chứa ID và trạng thái mới
        BillStatusDTO response = new BillStatusDTO(bill.getId(), bill.getBillStatus().name());
        return ApiResponse.Q_success(response, QuyExeption.SUCCESS);
    }
    // dùng để map
    public List<BillDetailDTO> getBillDetails(Bill bill) {
        return bill.getBillDetails().stream().map(detail -> new BillDetailDTO(
                detail.getId(),
                (long) detail.getProductBatch().getId(),
                detail.getQuantity(),
                detail.getPrice()
        )).collect(Collectors.toList());
    }

}
