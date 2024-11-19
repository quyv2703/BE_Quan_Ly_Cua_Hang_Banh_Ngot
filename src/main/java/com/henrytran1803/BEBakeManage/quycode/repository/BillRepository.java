package com.henrytran1803.BEBakeManage.quycode.repository;

import com.henrytran1803.BEBakeManage.quycode.entity.Area;
import com.henrytran1803.BEBakeManage.quycode.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    // Các phương thức mặc định: findById, save, deleteById, ...
}
