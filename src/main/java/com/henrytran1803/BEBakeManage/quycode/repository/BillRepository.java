package com.henrytran1803.BEBakeManage.quycode.repository;


import com.henrytran1803.BEBakeManage.quycode.BillStatus;

import com.henrytran1803.BEBakeManage.quycode.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.henrytran1803.BEBakeManage.user.dto.DashBoardDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;



@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Page<Bill> findByBillStatus(BillStatus status, Pageable pageable);
    List<Bill> findByBillStatusAndCreatedAtBetween(
            BillStatus status,
            LocalDateTime start,
            LocalDateTime end
    );

    List<Bill> findByBillStatusAndCreatedAtGreaterThanEqual(
            BillStatus status,
            LocalDateTime startDate
    );
    List<Bill> findByBillStatus(BillStatus paymentStatus);

    // Tìm kiếm theo ID (nếu id có thể là null, cần xử lý)
    // làm phần tìm kiếm
    Page<Bill> findByIdOrCustomerNameContainingOrCustomerPhoneContaining(Long id, String customerName, String customerPhone, Pageable pageable);
}

/*@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

}*/

