package com.henrytran1803.BEBakeManage.quycode.entity;

import com.henrytran1803.BEBakeManage.quycode.BillStatus;
import com.henrytran1803.BEBakeManage.user.entity.User;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bill_status_history")
@Data
@NoArgsConstructor
public class BillStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill; // Tham chiếu đến hóa đơn

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status", nullable = false)
    private BillStatus oldStatus; // Trạng thái cũ

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false)
    private BillStatus newStatus; // Trạng thái mới

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = true)
    private User updatedBy; // Người cập nhật (nullable để cho phép null khi không có user)

    @Column(name = "updated_by_system", nullable = true)
    private Boolean updatedBySystem; // Đánh dấu cập nhật tự động bởi hệ thống (true nếu là hệ thống)

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now(); // Thời gian cập nhật
}
