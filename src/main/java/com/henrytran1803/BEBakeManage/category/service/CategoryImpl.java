package com.henrytran1803.BEBakeManage.category.service;

import com.henrytran1803.BEBakeManage.category.dto.CreateCategoryDTO;
import com.henrytran1803.BEBakeManage.category.entity.Category;
import com.henrytran1803.BEBakeManage.category.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable; // Sửa dòng này
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
            existingCategory.setActive(false);
            categoryRepository.save(existingCategory);
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
        category.setActive(true);
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

        // Gán lại giá trị cho `active` nếu `category.active` là null
        category.setActive(category.getActive() != null ? category.getActive() : categoryToUpdate.get().getActive());

        // Lưu category đã cập nhật
        return Optional.of(categoryRepository.save(category));
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
}
