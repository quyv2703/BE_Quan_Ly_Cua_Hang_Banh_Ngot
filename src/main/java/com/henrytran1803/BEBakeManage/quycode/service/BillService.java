package com.henrytran1803.BEBakeManage.quycode.service;

import com.henrytran1803.BEBakeManage.common.exception.error.QuyExeption;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.product.entity.ProductBatch;
import com.henrytran1803.BEBakeManage.promotion.repository.ProductBatchRepository;
import com.henrytran1803.BEBakeManage.quycode.BillStatus;
import com.henrytran1803.BEBakeManage.quycode.PaymentMethod;
import com.henrytran1803.BEBakeManage.quycode.dto.BillDetailRequest;
import com.henrytran1803.BEBakeManage.quycode.dto.BillRequest;
import com.henrytran1803.BEBakeManage.quycode.entity.Bill;
import com.henrytran1803.BEBakeManage.quycode.entity.BillDetail;
import com.henrytran1803.BEBakeManage.quycode.entity.Table;
import com.henrytran1803.BEBakeManage.quycode.repository.BillRepository;
import com.henrytran1803.BEBakeManage.quycode.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private ProductBatchRepository productBatchRepository;

    public ApiResponse<Bill> createBill(BillRequest billRequest) {
        Bill bill = new Bill();
        bill.setCustomerName(billRequest.getCustomerName());
        bill.setCustomerPhone(billRequest.getCustomerPhone());
        bill.setPaymentMethod(PaymentMethod.valueOf(billRequest.getPaymentMethod()));
        bill.setPaymentStatus(BillStatus.valueOf(billRequest.getPaymentStatus()));

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
            bill.addBillDetail(billDetail);
        }

        billRepository.save(bill);
        return ApiResponse.Q_success(bill, QuyExeption.SUCCESS);
    }
}
