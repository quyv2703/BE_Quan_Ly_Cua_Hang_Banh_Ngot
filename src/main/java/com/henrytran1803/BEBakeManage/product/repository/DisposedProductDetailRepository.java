package com.henrytran1803.BEBakeManage.product.repository;

import com.henrytran1803.BEBakeManage.product.entity.DisposedProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisposedProductDetailRepository extends JpaRepository<DisposedProductDetail, Integer> {
}
