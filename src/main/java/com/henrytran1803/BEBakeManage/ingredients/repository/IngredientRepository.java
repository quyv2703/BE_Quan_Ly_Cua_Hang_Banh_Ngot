package com.henrytran1803.BEBakeManage.ingredients.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.henrytran1803.BEBakeManage.ingredients.entity.Ingredients;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredients, Integer> {
    @Transactional
    @Modifying
    @Query("UPDATE Ingredients i SET i.quantity = i.quantity + :amount WHERE i.id = :ingredientId")
    void increaseQuantity(int ingredientId, double amount);
    
    @Transactional
    @Modifying
    @Query("UPDATE Ingredients i SET i.quantity = i.quantity - :amount WHERE i.id = :ingredientId")
    void decreaseQuantity(int ingredientId, double amount);

    List<Ingredients> findByIsactiveTrue();
    
    @Query("SELECT i FROM Ingredients i WHERE i.isactive = true AND (i.name LIKE %:keyword% OR CAST(i.unit_id AS string) LIKE %:keyword%)")
    Page<Ingredients> searchByKeyword(String keyword, Pageable pageable);
}