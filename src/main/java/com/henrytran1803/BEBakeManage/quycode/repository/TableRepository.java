package com.henrytran1803.BEBakeManage.quycode.repository;




import com.henrytran1803.BEBakeManage.quycode.entity.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<Table, Long> {
    // Các phương thức mặc định: findById, save, deleteById, ...
}
