package com.example.socialmedia.service;

import com.example.socialmedia.exception.APIException;
import com.example.socialmedia.exception.ResourceNotFoundException;
import com.example.socialmedia.model.Category;
import com.example.socialmedia.payload.CategoryDTO;
import com.example.socialmedia.payload.CategoryResponse;
import com.example.socialmedia.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    public CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<Category> categories = categoryPage.getContent();
        if (categories == null)
            throw new APIException("no categories found ");
        List<CategoryDTO> response = categories.stream()
                .map(c -> modelMapper.map(c, CategoryDTO.class))
                .collect(Collectors.toList());
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(response);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setTotalElements(categoryPage.getContent().size());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (existingCategory != null)
            throw new APIException("Category already exists " + category.getCategoryName());
        categoryRepository.save(category);
        CategoryDTO response = modelMapper.map(category, CategoryDTO.class);
        return response;
    }

    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        categoryRepository.delete(category);
        CategoryDTO categoryDTO= modelMapper.map(category, CategoryDTO.class);
        return categoryDTO;
    }

    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Optional<Category> existingCategory = categoryRepository.findById(categoryId);
        Category categoryToUpdate = existingCategory.orElseThrow(
                () -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        categoryToUpdate.setCategoryId(categoryId);
        categoryToUpdate.setCategoryName(categoryDTO.getCategoryName());
                categoryRepository.save(categoryToUpdate);
        CategoryDTO categoryDTOToUpdate = modelMapper.map(categoryToUpdate, CategoryDTO.class);
        return categoryDTOToUpdate;
    }
}
