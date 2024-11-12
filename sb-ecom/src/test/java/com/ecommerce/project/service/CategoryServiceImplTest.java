package com.ecommerce.project.service;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void getAllCategories() {
       Integer pageNumber = 0;
       Integer pageSize = 10;
       String sortBy = AppConstants.SORT_CATEGORIES_BY;
       String sortOrder = AppConstants.SORT_DIR;

        Sort sort = Sort.by(sortBy).ascending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sort);
        Category category1 = new Category();
        category1.setCategoryId(1L);
        category1.setCategoryName("Category 1");

        Category category2 = new Category();
        category2.setCategoryId(2L);
        category2.setCategoryName("Category 2");

        List<Category> categoryList = List.of(category1, category2);
        Page<Category> categoryPage = new PageImpl<>(categoryList, pageDetails, categoryList.size());

        when(categoryRepository.findAll(pageDetails)).thenReturn(categoryPage);

        CategoryDTO categoryDTO1 = new CategoryDTO();
        categoryDTO1.setCategoryId(1L);
        categoryDTO1.setCategoryName("Category 1");

        CategoryDTO categoryDTO2 = new CategoryDTO();
        categoryDTO2.setCategoryId(2L);
        categoryDTO2.setCategoryName("Category 2");

        when(modelMapper.map(category1, CategoryDTO.class)).thenReturn(categoryDTO1);
        when(modelMapper.map(category2, CategoryDTO.class)).thenReturn(categoryDTO2);

        // Act
        CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);

        // Assert
        assertNotNull(categoryResponse);
        assertEquals(2, categoryResponse.getContent().size());
        assertEquals(2, categoryResponse.getTotalElements());
        assertTrue(categoryResponse.getContent().contains(categoryDTO1));
        assertTrue(categoryResponse.getContent().contains(categoryDTO2));
    }
}