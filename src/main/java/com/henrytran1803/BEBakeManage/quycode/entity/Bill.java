package com.henrytran1803.BEBakeManage.quycode.entity;

import com.henrytran1803.BEBakeManage.quycode.DiningOption;
import com.henrytran1803.BEBakeManage.quycode.PaymentMethod;
import com.henrytran1803.BEBakeManage.quycode.BillStatus;
import com.henrytran1803.BEBakeManage.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@jakarta.persistence.Table(name = "bills")
@Data
@NoArgsConstructor
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name", nullable = true, length = 100)
    private String customerName; // Tên khách hàng (nếu không đăng nhập)

    @Column(name = "customer_phone", nullable = true, length = 20)
    private String customerPhone; // Số điện thoại khách hàng (nếu không đăng nhập)

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod; // Phương thức thanh toán: "CASH" hoặc "QR_CODE"

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private BillStatus billStatus; // Trạng thái thanh toán: "NOT_PAID", "PAID", "COMPLETED"

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount = 0.0; // Tổng tiền của hóa đơn

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // Thời gian tạo bill

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = true) // Thông tin bàn, có thể null nếu không chọn
    private Table table;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillDetail> billDetails = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "dining_option", nullable = false)
    private DiningOption diningOption; // Thêm trạng thái 'Tại bàn' hoặc 'Mang về'



    public void addBillDetail(BillDetail billDetail) {
        billDetails.add(billDetail);
        billDetail.setBill(this);
        recalculateTotalAmount(); // Tính lại tổng tiền
    }

    public void removeBillDetail(BillDetail billDetail) {
        billDetails.remove(billDetail);
        billDetail.setBill(null);
        recalculateTotalAmount(); // Tính lại tổng tiền
    }

    public void recalculateTotalAmount() {
        this.totalAmount = billDetails.stream()
                .mapToDouble(billDetail -> billDetail.getPrice() * billDetail.getQuantity())
                .sum();
    }
}
