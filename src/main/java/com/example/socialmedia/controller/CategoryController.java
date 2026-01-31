package com.example.socialmedia.controller;

import com.example.socialmedia.config.AppConstants;
import com.example.socialmedia.model.Category;
import com.example.socialmedia.payload.CategoryDTO;
import com.example.socialmedia.payload.CategoryResponse;
import com.example.socialmedia.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER)Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE)Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_CATEGORIES_BY)String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_ORDER)String sortOrder
    ) {
        CategoryResponse categories = categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO saveCategory = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(saveCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/categoryId")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
        CategoryDTO deletedCategory = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(deletedCategory, HttpStatus.OK);
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updateCategory = categoryService.updateCategory(categoryId, categoryDTO);
        return new ResponseEntity<>(updateCategory, HttpStatus.OK);
    }

}
