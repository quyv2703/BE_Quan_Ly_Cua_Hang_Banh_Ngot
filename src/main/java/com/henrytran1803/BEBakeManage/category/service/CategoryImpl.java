package com.henrytran1803.BEBakeManage.category.service;

import com.henrytran1803.BEBakeManage.category.dto.CategorySearchCriteria;
import com.henrytran1803.BEBakeManage.category.dto.CreateCategoryDTO;
import com.henrytran1803.BEBakeManage.category.entity.Category;
import com.henrytran1803.BEBakeManage.category.repository.CategoryRepository;
import com.henrytran1803.BEBakeManage.category.specification.CategorySpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable; // Sửa dòng này
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Optional<Category> getById(int id) {
        return categoryRepository.findById(id); // Thay đổi để lấy category theo id
    }

    @Override
    public Optional<Category> deleteById(int id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            Category existingCategory = category.get();
            if (existingCategory.getIsActive()){
                existingCategory.setIsActive(false);
                categoryRepository.save(existingCategory);
            }else {
                existingCategory.setIsActive(true);
                categoryRepository.save(existingCategory);
            }

        }
        return category;
    }


    @Override
    public Optional<Category> createCategory(CreateCategoryDTO newCategory) {
        Optional<Category> existingCategory = categoryRepository.findFirstByName(newCategory.getName());
        if (existingCategory.isPresent()) {
            return Optional.empty();
        }
        Category category = new Category();
        category.setName(newCategory.getName());
        category.setImageUrl(newCategory.getUrl());
        category.setIsActive(true);
        Category savedCategory = categoryRepository.save(category);
        return Optional.of(savedCategory);
    }

    @Override
    public Optional<Category> updateById(Category category) {
        Optional<Category> categoryToUpdate = categoryRepository.findById(category.getId());
        if (categoryToUpdate.isEmpty()) {
            return Optional.empty();
        }

        Optional<Category> existingCategory = categoryRepository.findFirstByName(category.getName());
        if (existingCategory.isPresent() && existingCategory.get().getId() != category.getId()) {
            throw new RuntimeException("Category name already exists.");
        }

        category.setIsActive(category.getIsActive() != null ? category.getIsActive() : categoryToUpdate.get().getIsActive());

        return Optional.of(categoryRepository.save(category));
    }


    public Page<Category> searchCategories(CategorySearchCriteria criteria) {
        Sort sort = Sort.by(
                criteria.getSortDir() == null || criteria.getSortDir().equalsIgnoreCase("asc")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC,
                criteria.getSortBy() == null ? "id" : criteria.getSortBy()
        );

        PageRequest pageRequest = PageRequest.of(
                criteria.getPage(),
                criteria.getSize(),
                sort
        );

        return categoryRepository.findAll(
                CategorySpecification.getSpecification(criteria),
                pageRequest
        );
    }

    @Override
    public Page<Category> getCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryRepository.findAll(pageable);
    }

    @Override
    public List<Category> getCategoriesActive() {
        return categoryRepository.findAllByIsActiveTrue();
    }

    @Override
    public List<Category> getAllCateories() {
        return categoryRepository.findAll();
    }
}
