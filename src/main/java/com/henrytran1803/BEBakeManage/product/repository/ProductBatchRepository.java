package com.henrytran1803.BEBakeManage.product.repository;

import com.henrytran1803.BEBakeManage.product.dto.ProductBatchDetailDTO;
import com.henrytran1803.BEBakeManage.product.entity.ProductBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductBatchRepository extends JpaRepository<ProductBatch,Integer> {
    List<ProductBatch> findByProductIdAndStatusIn(int productId, List<String> statuses);

//    @Procedure(name = "GetProductBatchDetailsByStatuses")
//    List<ProductBatchDetailDTO> getProductBatchDetailsByStatuses(@Param("statuses") String statuses);
@Query(value = """
        SELECT 
            pb.id AS id,
            p.name AS name,
            pb.status AS status,
            COALESCE(pb.quantity, 0) AS quantity,
            DATE(pb.expiration_date) AS dateExpiry,
            DATEDIFF(DATE(pb.expiration_date), CURDATE()) AS countDown
        FROM 
            products p
            INNER JOIN product_batches pb ON p.id = pb.product_id
        WHERE 
            pb.status IN (:statuses)
        """, nativeQuery = true)
List<Object[]> findProductBatchDetailsByStatuses(@Param("statuses") List<String> statuses);
}
