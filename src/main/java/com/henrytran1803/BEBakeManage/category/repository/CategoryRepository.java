package com.henrytran1803.BEBakeManage.category.repository;
import com.henrytran1803.BEBakeManage.category.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends  JpaRepository<Category,Integer>{
    Page<Category> findAll(Pageable pageable);
    Optional<Category> findFirstByName(String name);
    List<Category> findAllByIsActiveTrue();

}
