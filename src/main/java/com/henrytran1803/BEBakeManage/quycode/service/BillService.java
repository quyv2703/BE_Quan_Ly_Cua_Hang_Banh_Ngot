package com.henrytran1803.BEBakeManage.quycode.service;

import com.henrytran1803.BEBakeManage.common.exception.error.QuyExeption;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.product.entity.Product;
import com.henrytran1803.BEBakeManage.product_batches.entity.ProductBatch;

import com.henrytran1803.BEBakeManage.product_batches.repository.ProductBatchRepository;
import com.henrytran1803.BEBakeManage.quycode.BillStatus;
import com.henrytran1803.BEBakeManage.quycode.PaymentMethod;
import com.henrytran1803.BEBakeManage.quycode.dto.BillDetailDTO;
import com.henrytran1803.BEBakeManage.quycode.dto.BillDetailDTO_ViewCake;
import com.henrytran1803.BEBakeManage.quycode.dto.BillStatusHistoryDTO;
import com.henrytran1803.BEBakeManage.quycode.entity.BillStatusHistory;
import com.henrytran1803.BEBakeManage.quycode.repository.BillStatusHistoryRepository;
import com.henrytran1803.BEBakeManage.quycode.request.BillDetailRequest;
import com.henrytran1803.BEBakeManage.quycode.request.BillRequest;
import com.henrytran1803.BEBakeManage.quycode.dto.BillStatusDTO;
import com.henrytran1803.BEBakeManage.quycode.entity.Bill;
import com.henrytran1803.BEBakeManage.quycode.entity.BillDetail;
import com.henrytran1803.BEBakeManage.quycode.entity.Table;
import com.henrytran1803.BEBakeManage.quycode.repository.BillRepository;
import com.henrytran1803.BEBakeManage.quycode.repository.TableRepository;
import com.henrytran1803.BEBakeManage.quycode.response.BillResponse;
import com.henrytran1803.BEBakeManage.quycode.response.BillResponseNoDetail;
import com.henrytran1803.BEBakeManage.quycode.response.BillResponse_View_Cake;
import com.henrytran1803.BEBakeManage.user.entity.User;
import com.henrytran1803.BEBakeManage.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BillStatusHistoryRepository billStatusHistoryRepository;






    public ApiResponse<Page<BillResponseNoDetail>> getBillsByStatus(BillStatus status, Pageable pageable) {
        // Lấy danh sách hóa đơn với phân trang
        Page<Bill> billPage = billRepository.findByBillStatus(status, pageable);

        // Chuyển đổi danh sách hóa đơn thành danh sách phản hồi mà không có chi tiết
        Page<BillResponseNoDetail> responsePage = billPage.map(bill -> {
            BillResponseNoDetail responseNoDetail = new BillResponseNoDetail();
            responseNoDetail.setBillId(bill.getId());
            responseNoDetail.setCustomerName(bill.getCustomerName());
            responseNoDetail.setCustomerPhone(bill.getCustomerPhone());
            responseNoDetail.setPaymentMethod(bill.getPaymentMethod().name());
            responseNoDetail.setBillStatus(bill.getBillStatus().name());
            responseNoDetail.setDiningOption(String.valueOf(bill.getDiningOption()));
            responseNoDetail.setTotalAmount(bill.getTotalAmount());
            return responseNoDetail;
        });

        // Trả về kết quả bao bọc trong ApiResponse
        return ApiResponse.Q_success(responsePage, QuyExeption.SUCCESS);
    }




    public ApiResponse<BillResponse_View_Cake> getBillDetailsById(Long billId) {
        Optional<Bill> billOptional = billRepository.findById(billId);
        if (billOptional.isEmpty()) {
            return ApiResponse.Q_failure(null, QuyExeption.BILL_NOT_FOUND);
        }
        Bill bill = billOptional.get();

        // Chuẩn bị thông tin phản hồi
        BillResponse_View_Cake response = new BillResponse_View_Cake();
        response.setBillId(bill.getId());
        response.setCustomerName(bill.getCustomerName());
        response.setCustomerPhone(bill.getCustomerPhone());
        response.setPaymentMethod(bill.getPaymentMethod().name());
        response.setNameArea(bill.getTable().getArea().getName());
        response.setNameArea(bill.getTable().getName());
        response.setBillStatus(bill.getBillStatus().name());
        response.setDiningOption(bill.getDiningOption());
        response.setTotalAmount(bill.getTotalAmount());


        // Lấy danh sách chi tiết hóa đơn
        List<BillDetailDTO_ViewCake> billDetails = bill.getBillDetails().stream().map(detail -> {
            ProductBatch productBatch = detail.getProductBatch();
            Product product = productBatch.getProduct();

    /*        // Tính tổng giảm giá
            double promotionDiscount = productBatch.getPromotionDetails() != null
                    ? productBatch.getPromotionDetails().stream()
                    .filter(promo -> promo.getPromotion().getIsActive() &&
                            !LocalDateTime.now().isBefore(promo.getPromotion().getStartDate()) &&
                            !LocalDateTime.now().isAfter(promo.getPromotion().getEndDate()))
                    .mapToDouble(promo -> promo.getPromotion().getDiscount())
                    .max().orElse(0.0)
                    : 0.0;
            double dailyDiscount = productBatch.getDailyDiscount() != null ? productBatch.getDailyDiscount() : 0.0;

            // Tính giá cuối cùng
            double finalPrice = detail.getPrice();
*/
            // Map thông tin chi tiết hóa đơn sang DTO
            return new BillDetailDTO_ViewCake(
                    detail.getId(),
                    (long) productBatch.getId(),
                    product.getName(),
                    product.getImages().stream().map(image -> image.getUrl()).collect(Collectors.toList()), // Ảnh sản phẩm
                    detail.getQuantity(),
                    /*bill.getTotalAmount(),*/
                    productBatch.getExpirationDate(),
                    productBatch.getStatus()/*,
                    dailyDiscount,
                    promotionDiscount*/
            );
        }).collect(Collectors.toList());

        response.setBillDetails(billDetails);

        // Trả về phản hồi
        return ApiResponse.Q_success(response, QuyExeption.SUCCESS);
    }

    //tạo bill
   /* public ApiResponse<BillResponse> createBill(BillRequest billRequest) {
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
    }*/
    // Tạo hóa đơn
    @Transactional
    public ApiResponse<BillResponse> createBill(BillRequest billRequest) {
        // Tạo đối tượng Bill
        Bill bill = new Bill();
        bill.setCustomerName(billRequest.getCustomerName());
        bill.setCustomerPhone(billRequest.getCustomerPhone());
        bill.setPaymentMethod(PaymentMethod.valueOf(billRequest.getPaymentMethod()));
        bill.setBillStatus(BillStatus.NOT_PAID);
        bill.setDiningOption(billRequest.getDiningOption()); // Thêm trạng thái

        // Gán thông tin bàn nếu có
        if (billRequest.getTableId() != null) {
            Optional<Table> tableOptional = tableRepository.findById(billRequest.getTableId());
            if (tableOptional.isEmpty()) {
                return ApiResponse.Q_failure(null, QuyExeption.TABLE_NOT_FOUND);
            }
            Table table = tableOptional.get();
            if (!table.isActive()) {
                return ApiResponse.Q_failure(null, QuyExeption.TABLE_INACTIVE);
            }
            bill.setTable(table);
        }

        double totalAmount = 0.0;

        // Xử lý danh sách chi tiết hóa đơn
        for (BillDetailRequest detailRequest : billRequest.getBillDetails()) {
            Optional<ProductBatch> productBatchOptional = productBatchRepository.findById(Math.toIntExact(detailRequest.getProductBatchId()));
            if (productBatchOptional.isEmpty()) {
                return ApiResponse.Q_failure(null, QuyExeption.PRODUCT_BATCH_NOT_FOUND);
            }

            ProductBatch productBatch = productBatchOptional.get();

            // Kiểm tra tồn kho
            if (productBatch.getQuantity() < detailRequest.getQuantity()) {
                return ApiResponse.Q_failure(null, QuyExeption.INSUFFICIENT_STOCK);
            }

            // Kiểm tra hạn sử dụng
            if (productBatch.getExpirationDate().isBefore(LocalDateTime.now())) {
                return ApiResponse.Q_failure(null, QuyExeption.PRODUCT_BATCH_EXPIRED);
            }

            // Tính giảm giá
            double dailyDiscount = productBatch.getDailyDiscount() != null ? productBatch.getDailyDiscount() : 0.0;

            // Kiểm tra Promotion (nếu có)
            double promotionDiscount = 0.0;
            if (productBatch.getPromotionDetails() != null) {
                promotionDiscount = productBatch.getPromotionDetails().stream()
                        .filter(detail -> detail.getPromotion().getIsActive()
                                && !LocalDateTime.now().isBefore(detail.getPromotion().getStartDate())
                                && !LocalDateTime.now().isAfter(detail.getPromotion().getEndDate()))
                        .mapToDouble(detail -> detail.getPromotion().getDiscount())
                        .max().orElse(0.0);
            }

            // Tính giá cuối cùng
            double finalPrice = productBatch.getProduct().getCurrentPrice() * (1 - dailyDiscount / 100)*(1-promotionDiscount/100);

            // Tạo BillDetail
            BillDetail billDetail = new BillDetail();
            billDetail.setProductBatch(productBatch);
            billDetail.setQuantity(detailRequest.getQuantity());
            billDetail.setPrice(finalPrice);

            // Cộng dồn tổng tiền
            totalAmount += finalPrice * detailRequest.getQuantity();

            // Cập nhật tồn kho
            productBatch.setQuantity(productBatch.getQuantity() - detailRequest.getQuantity());

            // Thêm BillDetail vào Bill
            bill.addBillDetail(billDetail);
        }

        // Gán tổng tiền vào Bill
        bill.setTotalAmount(totalAmount);

        // Lưu hóa đơn vào cơ sở dữ liệu
        billRepository.save(bill);

        // Chuyển đổi Bill sang DTO
        BillResponse billResponse = new BillResponse();
        billResponse.setBillId(bill.getId());
        billResponse.setCustomerName(bill.getCustomerName());
        billResponse.setCustomerPhone(bill.getCustomerPhone());
        billResponse.setPaymentMethod(bill.getPaymentMethod().name());
        billResponse.setBillStatus(bill.getBillStatus().name());
        billResponse.setTotalAmount(bill.getTotalAmount());
        billResponse.setDiningOption(bill.getDiningOption());
        billResponse.setBillDetails(getBillDetails(bill));

        return ApiResponse.Q_success(billResponse, QuyExeption.SUCCESS);
    }

   /* // Phương thức để map BillDetails sang DTO
    private List<BillDetailDTO> getBillDetails(Bill bill) {
        return bill.getBillDetails().stream().map(detail -> new BillDetailDTO(
                detail.getId(),
                (long) detail.getProductBatch().getId(),
                detail.getQuantity(),
                detail.getPrice()
        )).collect(Collectors.toList());
    }*/
   @Transactional
   public ApiResponse<BillStatusDTO> updateBillStatus(Long billId, BillStatus newStatus, User user) {
       // Tìm hóa đơn theo ID
       Optional<Bill> billOptional = billRepository.findById(billId);
       if (billOptional.isEmpty()) {
           return ApiResponse.Q_failure(null, QuyExeption.BILL_NOT_FOUND);
       }

       Bill bill = billOptional.get();
       BillStatus oldStatus = bill.getBillStatus();

       // Kiểm tra trạng thái không hợp lệ (PAID -> CANCEL)
       if (oldStatus == BillStatus.PAID && newStatus == BillStatus.CANCEL) {
           return ApiResponse.Q_failure(null, QuyExeption.INVALID_STATUS_TRANSITION);
       }

       // Kiểm tra nếu trạng thái mới giống trạng thái hiện tại
       if (oldStatus == newStatus) {
           return ApiResponse.Q_failure(null, QuyExeption.BILL_STATUS_ALREADY_UPDATED);
       }

       // Nếu trạng thái mới là CANCEL, hoàn trả số lượng sản phẩm
       if (newStatus == BillStatus.CANCEL) {
           for (BillDetail detail : bill.getBillDetails()) {
               ProductBatch productBatch = detail.getProductBatch();
               productBatch.setQuantity(productBatch.getQuantity() + detail.getQuantity());
               productBatchRepository.save(productBatch); // Cập nhật lại kho
           }
       }

       // Cập nhật trạng thái hóa đơn
       bill.setBillStatus(newStatus);
       billRepository.save(bill);

       // Lưu lịch sử trạng thái
       BillStatusHistory history = new BillStatusHistory();
       history.setBill(bill);
       history.setOldStatus(oldStatus);
       history.setNewStatus(newStatus);
       history.setUpdatedAt(LocalDateTime.now());

       if (user == null) {
           history.setUpdatedBySystem(true); // Đánh dấu cập nhật bởi hệ thống
           history.setUpdatedBy(null); // Không có người dùng nào
       } else {
           history.setUpdatedBy(user);
           history.setUpdatedBySystem(false); // Cập nhật bởi người dùng
       }

       billStatusHistoryRepository.save(history);

       // Chuẩn bị dữ liệu trả về
       BillStatusDTO response = new BillStatusDTO();
       response.setBillId(bill.getId());
       response.setOldStatus(oldStatus.name());
       response.setNewStatus(newStatus.name());
       response.setUpdatedBy(user != null ? user.getEmail() : "SYSTEM");
       response.setUpdatedAt(history.getUpdatedAt());

       return ApiResponse.Q_success(response, QuyExeption.SUCCESS);
   }




    //
    public ApiResponse<List<BillStatusHistoryDTO>> getBillStatusHistory(Long billId) {
        List<BillStatusHistory> histories = billStatusHistoryRepository.findByBillId(billId);
        if (histories.isEmpty()) {
            return ApiResponse.Q_failure(null, QuyExeption.NO_HISTORY_FOUND);
        }

        List<BillStatusHistoryDTO> historyDTOs = histories.stream().map(history -> new BillStatusHistoryDTO(
                history.getId(),
                history.getBill().getId(),
                history.getOldStatus().name(),
                history.getNewStatus().name(),
                (long) history.getUpdatedBy().getId(),
                history.getUpdatedBy().getFirstName() + " " + history.getUpdatedBy().getLastName(),
                history.getUpdatedAt()
        )).collect(Collectors.toList());

        return ApiResponse.Q_success(historyDTOs, QuyExeption.SUCCESS);
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
