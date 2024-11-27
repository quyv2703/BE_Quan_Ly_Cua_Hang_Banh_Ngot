package com.henrytran1803.BEBakeManage.quycode.repository;


import com.henrytran1803.BEBakeManage.quycode.BillStatus;

import com.henrytran1803.BEBakeManage.quycode.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Page<Bill> findByBillStatus(BillStatus status, Pageable pageable);
}

