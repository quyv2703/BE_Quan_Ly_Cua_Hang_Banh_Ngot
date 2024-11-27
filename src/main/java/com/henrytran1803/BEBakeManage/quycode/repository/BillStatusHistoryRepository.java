package com.henrytran1803.BEBakeManage.quycode.repository;

import com.henrytran1803.BEBakeManage.quycode.entity.BillStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillStatusHistoryRepository extends JpaRepository<BillStatusHistory, Long> {
    List<BillStatusHistory> findByBillId(Long billId);
}
