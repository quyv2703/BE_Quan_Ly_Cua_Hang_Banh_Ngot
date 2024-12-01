package com.henrytran1803.BEBakeManage.disposed_product.repository;

import com.henrytran1803.BEBakeManage.disposed_product.entity.DisposedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisposedProductRepository extends JpaRepository<DisposedProduct, Integer> {
}
