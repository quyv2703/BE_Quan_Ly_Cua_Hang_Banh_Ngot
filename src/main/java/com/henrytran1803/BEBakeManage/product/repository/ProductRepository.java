package com.henrytran1803.BEBakeManage.product.repository;

import com.henrytran1803.BEBakeManage.product.dto.ProductDetailProjection;
import com.henrytran1803.BEBakeManage.product.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    @Query("SELECT p FROM Product p " +
            "LEFT JOIN FETCH p.images " +
            "LEFT JOIN FETCH p.category " +
            "LEFT JOIN FETCH p.recipe " +
            "WHERE p.id = :id")
    Optional<Product> findById(@Param("id") Integer id);
    boolean existsByName(@NotBlank(message = "Name is required") @Size(max = 250, message = "Name must not exceed 250 characters") String name);
    List<Product> findByStatusTrue();

    @Query(value = "SELECT * FROM v_product_details", nativeQuery = true)
    List<ProductDetailProjection> findAllProductDetails();

}
