package com.henrytran1803.BEBakeManage.category.controller;

import com.henrytran1803.BEBakeManage.category.dto.CreateCategoryDTO;
import com.henrytran1803.BEBakeManage.category.entity.Category;
import com.henrytran1803.BEBakeManage.category.service.CategoryService;
import com.henrytran1803.BEBakeManage.common.exception.error.ErrorCode;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @PostMapping("")
    public ResponseEntity<ApiResponse<Category>> createCategory(@Valid @RequestBody CreateCategoryDTO createCategoryDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.CATEGORY_CREATE_FAIL.name(), "Invalid category data"));
        }

        Optional<Category> createdCategory = categoryService.createCategory(createCategoryDTO);
        if (createdCategory.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(createdCategory.get()));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.CATEGORY_CREATE_FAIL.name(), "Could not create category"));
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> getCategoryById(@PathVariable int id) {
        Optional<Category> category = categoryService.getById(id);
        if (category.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(category.get()));
        } else {
            return ResponseEntity.status(404).body(ApiResponse.error(ErrorCode.CATEGORY_NOT_FOUND.name(), "Category not found"));
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategoryById(@PathVariable int id) {
        Optional<Category> deletedCategory = categoryService.deleteById(id);
        if (deletedCategory.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("Category deleted successfully"));
        } else {
            return ResponseEntity.status(404).body(ApiResponse.error(ErrorCode.CATEGORY_DELETE_FAILED.name(), "Could not delete category"));
        }
    }
    @PutMapping("")
    public ResponseEntity<ApiResponse<Category>> updateCategoryById(@Valid @RequestBody Category category, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.CATEGORY_UPDATE_FAILED.name(), "Invalid category data"));
        }

        Optional<Category> updatedCategory = categoryService.updateById(category);
        if (updatedCategory.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(updatedCategory.get()));
        } else {
            return ResponseEntity.status(404).body(ApiResponse.error(ErrorCode.CATEGORY_UPDATE_FAILED.name(), "Could not update category"));
        }
    }
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Page<Category>>> getCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Category> categories = categoryService.getCategories(page, size);
        return ResponseEntity.ok(ApiResponse.success(categories));
    }
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Category>>> getCategoriesActive() {
        List<Category> categories = categoryService.getCategoriesActive();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }
}
