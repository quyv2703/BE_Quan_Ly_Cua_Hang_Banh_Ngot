package com.henrytran1803.BEBakeManage.quycode.repository;

import com.henrytran1803.BEBakeManage.quycode.entity.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail, Long> {

}