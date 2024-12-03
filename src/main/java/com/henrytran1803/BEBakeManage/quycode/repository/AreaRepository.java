package com.henrytran1803.BEBakeManage.quycode.repository;


import com.henrytran1803.BEBakeManage.quycode.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {
    // Các phương thức mặc định: findById, save, deleteById, ...
}
