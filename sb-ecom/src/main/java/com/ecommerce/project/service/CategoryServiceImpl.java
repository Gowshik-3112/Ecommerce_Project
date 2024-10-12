package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Page<Category> pageRetrieved = categoryRepository.findAll(pageDetails);

        List<Category> categories = pageRetrieved.getContent();
        if (categories.isEmpty()) {
            throw new APIException("categories needs to be added");
        }

        List<CategoryDTO> categoryDTOS = categories.stream().
                map(category -> modelMapper.map(category, CategoryDTO.class)).
                toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setTotalElements(pageRetrieved.getTotalElements());
        categoryResponse.setPageNumber(pageRetrieved.getNumber());
        categoryResponse.setPageSize(pageRetrieved.getSize());
        categoryResponse.setTotalpages(pageRetrieved.getTotalPages());
        categoryResponse.setLastPage(pageRetrieved.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (savedCategory != null) {
            throw new APIException("Category already exists with same name " + category.getCategoryName());
        }
        Category createdCategory =  categoryRepository.save(category);
        return modelMapper.map(createdCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO category, Long categoryId) {

        Category updatedCategory = categoryRepository.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        Category createdCategory = modelMapper.map(category, Category.class);
        createdCategory.setCategoryId(categoryId);
        System.out.println(category.getCategoryName() + " " + category.getCategoryId());

        updatedCategory = categoryRepository.save(createdCategory);
        return modelMapper.map(updatedCategory, CategoryDTO.class);
    }
}
