package com.example.socialmedia.service;

import com.example.socialmedia.model.Category;
import com.example.socialmedia.payload.CategoryDTO;
import com.example.socialmedia.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(Long categoryId,CategoryDTO categoryDTO);
}
