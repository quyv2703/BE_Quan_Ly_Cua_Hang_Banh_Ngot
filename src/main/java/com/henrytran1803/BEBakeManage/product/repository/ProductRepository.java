package com.henrytran1803.BEBakeManage.product.repository;


import com.henrytran1803.BEBakeManage.product.dto.ProductDetailProjection;
import com.henrytran1803.BEBakeManage.product.entity.Product;
import com.henrytran1803.BEBakeManage.recipe.entity.Recipe;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<Product> findByNameContainingIgnoreCaseAndStatusIsTrue(
            String name,
            Pageable pageable
    );

//    Page<Product> findByCategoryIdInAndStatusIsTrue(
//            List<Integer> categoryIds,
//            Pageable pageable
//    );
//
//    Page<Product> findByCategoryIdInAndNameContainingIgnoreCaseAndStatusIsTrue(
//            List<Integer>  categoryIds,
//            String name,
//            Pageable pageable
//    );

    @Query("SELECT p FROM Product p WHERE p.category.id IN :categoryIds AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.status = true")
    Page<Product> findByCategoryIdInAndNameContainingIgnoreCaseAndStatusIsTrue(@Param("categoryIds") List<Integer> categoryIds, @Param("name") String name, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id IN :categoryIds AND p.status = true")
    Page<Product> findByCategoryIdInAndStatusIsTrue(@Param("categoryIds") List<Integer> categoryIds, Pageable pageable);

    Page<Product> findByStatusIsTrue(Pageable pageable);

}
