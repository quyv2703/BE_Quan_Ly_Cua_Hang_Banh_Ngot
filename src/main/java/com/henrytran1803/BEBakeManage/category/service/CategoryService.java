package com.henrytran1803.BEBakeManage.category.service;

import com.henrytran1803.BEBakeManage.category.dto.CategorySearchCriteria;
import com.henrytran1803.BEBakeManage.category.dto.CreateCategoryDTO;
import com.henrytran1803.BEBakeManage.category.entity.Category;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Optional<Category> getById(int id);
    Optional<Category> deleteById(int id);
    Optional<Category> createCategory(CreateCategoryDTO newCategory);
    Optional<Category> updateById(Category category);
    Page<Category> getCategories(int page, int size);
    List<Category> getCategoriesActive();
    List<Category> getAllCateories();
    Page<Category> searchCategories(CategorySearchCriteria criteria);
}
