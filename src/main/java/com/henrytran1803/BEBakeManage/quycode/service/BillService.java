package com.henrytran1803.BEBakeManage.quycode.service;

import com.henrytran1803.BEBakeManage.common.exception.error.QuyExeption;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.nofication.model.NotificationMessage;
import com.henrytran1803.BEBakeManage.nofication.service.NotificationService;
import com.henrytran1803.BEBakeManage.product.entity.Product;
import com.henrytran1803.BEBakeManage.product_batches.entity.ProductBatch;

import com.henrytran1803.BEBakeManage.product_batches.repository.ProductBatchRepository;
import com.henrytran1803.BEBakeManage.quycode.BillStatus;
import com.henrytran1803.BEBakeManage.quycode.PaymentMethod;
import com.henrytran1803.BEBakeManage.quycode.dto.BillDetailDTO;
import com.henrytran1803.BEBakeManage.quycode.dto.BillDetailDTO_ViewCake;
import com.henrytran1803.BEBakeManage.quycode.dto.BillStatusHistoryDTO;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.threeten.bp.ZoneId;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private NotificationService notificationService;






    // API tìm kiếm theo ID, tên khách hàng hoặc số điện thoại với phân trang
    public ApiResponse<Page<BillResponseNoDetail>> searchBills(Long id, String customerName, String customerPhone, Pageable pageable) {
        // Lấy danh sách hóa đơn với phân trang
        Page<Bill> billPage = billRepository.findByIdOrCustomerNameContainingOrCustomerPhoneContaining(id, customerName, customerPhone, pageable);

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
        return ApiResponse.success(responsePage);
    }



    public ApiResponse<Page<BillResponseNoDetail>> getBillsByStatus(BillStatus status, Pageable pageable) {
        // Create a PageRequest with sorting by createdAt in descending order
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        // Get paginated bills with sorting
        Page<Bill> billPage = billRepository.findByBillStatus(status, sortedPageable);

        // Convert to response DTOs
        Page<BillResponseNoDetail> responsePage = billPage.map(bill -> {
            BillResponseNoDetail responseNoDetail = new BillResponseNoDetail();
            responseNoDetail.setBillId(bill.getId());
            responseNoDetail.setCustomerName(bill.getCustomerName());
            responseNoDetail.setCustomerPhone(bill.getCustomerPhone());
            responseNoDetail.setPaymentMethod(bill.getPaymentMethod().name());
            responseNoDetail.setBillStatus(bill.getBillStatus().name());
            responseNoDetail.setDiningOption(String.valueOf(bill.getDiningOption()));
            LocalDateTime createdAt = bill.getCreatedAt();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String formattedDateTime = createdAt.format(formatter);
            responseNoDetail.setCreatedAt(formattedDateTime);
            responseNoDetail.setTotalAmount(bill.getTotalAmount());
            return responseNoDetail;
        });

        return ApiResponse.Q_success(responsePage, QuyExeption.SUCCESS);
    }



    @Transactional
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
        response.setNameTable(bill.getTable().getName());
        response.setBillStatus(bill.getBillStatus().name());
        response.setDiningOption(bill.getDiningOption());
        response.setTotalAmount(bill.getTotalAmount());

        // Lấy danh sách chi tiết hóa đơn
        List<BillDetailDTO_ViewCake> billDetails = bill.getBillDetails().stream().map(detail -> {
            ProductBatch productBatch = detail.getProductBatch();
            Product product = productBatch.getProduct();

            /*// Tính tổng giảm giá (promotion)
            double promotionDiscount = 0.0;
            if (productBatch.getPromotionDetails() != null) {
                promotionDiscount = productBatch.getPromotionDetails().stream()
                        .filter(promo -> promo.getPromotion().getIsActive() &&
                                !LocalDateTime.now().isBefore(promo.getPromotion().getStartDate()) &&
                                !LocalDateTime.now().isAfter(promo.getPromotion().getEndDate()))
                        .mapToDouble(promo -> promo.getPromotion().getDiscount())
                        .max()
                        .orElse(0.0);
            }*/

            // Tạo BillDetailDTO_ViewCake
            BillDetailDTO_ViewCake detailDTO = new BillDetailDTO_ViewCake();
            detailDTO.setId(detail.getId());
            detailDTO.setProductBatchId((long) productBatch.getId());
            detailDTO.setProductImages(product.getImages().get(0).getUrl());
            detailDTO.setProductName(product.getName());
            detailDTO.setQuantity(detail.getQuantity());
            detailDTO.setPrice(detail.getPrice());
            detailDTO.setExpirationDate(productBatch.getExpirationDate());



            return detailDTO;
        }).collect(Collectors.toList());

        // Thêm danh sách chi tiết vào phản hồi
        response.setBillDetails(billDetails);

        return ApiResponse.Q_success(response,QuyExeption.SUCCESS);
    }



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

            // Tính giá cuối cùng của productbatch sau giảm giá
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

   public ApiResponse<BillStatusDTO> updateBillStatus(Long billId, BillStatus newStatus) {


       Optional<Bill> billOptional = billRepository.findById(billId);
       if (billOptional.isEmpty()) {
           return ApiResponse.Q_failure(null, QuyExeption.BILL_NOT_FOUND);
       }

       Bill bill = billOptional.get();
       BillStatus oldStatus = bill.getBillStatus();

       if (oldStatus == BillStatus.PAID && newStatus == BillStatus.CANCEL) {

           notificationService.sendPaymentNotification(Math.toIntExact(billId),"Đã thanh toán không cho huỷ", NotificationMessage.MessageSeverity.ERROR);
           return ApiResponse.Q_failure(null, QuyExeption.INVALID_STATUS_TRANSITION);
       }

       // Kiểm tra nếu trạng thái mới giống trạng thái hiện tại
       if (oldStatus == newStatus) {
           notificationService.sendNotification(
                   "Hóa đơn #" + billId + " đã ở trạng thái " + newStatus,
                   NotificationMessage.MessageSeverity.WARNING
           );
           return ApiResponse.Q_failure(null, QuyExeption.BILL_STATUS_ALREADY_UPDATED);
       }

       if (newStatus == BillStatus.CANCEL) {
           for (BillDetail detail : bill.getBillDetails()) {
               notificationService.sendPaymentNotification(Math.toIntExact(billId),"Đơn hàng đã buỷ", NotificationMessage.MessageSeverity.INFO);
               ProductBatch productBatch = detail.getProductBatch();
               productBatch.setQuantity(productBatch.getQuantity() + detail.getQuantity());
               productBatchRepository.save(productBatch); // Cập nhật lại kho
           }
           notificationService.sendNotification(
                   "Đã hoàn trả số lượng sản phẩm cho hóa đơn #" + billId,
                   NotificationMessage.MessageSeverity.INFO
           );
       }

       // Cập nhật trạng thái hóa đơn
       bill.setBillStatus(newStatus);
       billRepository.save(bill);

// <<<<<<< quy
// =======
//        BillStatusHistory history = new BillStatusHistory();
//        history.setBill(bill);
//        history.setOldStatus(oldStatus);
//        history.setNewStatus(newStatus);
//        history.setUpdatedAt(LocalDateTime.now());

//        if (user == null) {
//            history.setUpdatedBySystem(true);
//            history.setUpdatedBy(null);
//        } else {
//            history.setUpdatedBy(user);
//            history.setUpdatedBySystem(false);
//        }

//        billStatusHistoryRepository.save(history);

// >>>>>>> main
       // Chuẩn bị dữ liệu trả về
       BillStatusDTO response = new BillStatusDTO();
       response.setBillId(bill.getId());
       response.setOldStatus(oldStatus.name());
       response.setNewStatus(newStatus.name());
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
