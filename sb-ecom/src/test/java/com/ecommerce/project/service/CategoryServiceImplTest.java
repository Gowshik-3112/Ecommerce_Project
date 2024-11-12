package com.ecommerce.project.service;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    static Category category1;
    static Category category2;

    @BeforeAll
    public static void init() {
        category1 = new Category();
        category1.setCategoryId(1L);
        category1.setCategoryName("Category 1");

        category2 = new Category();
        category2.setCategoryId(2L);
        category2.setCategoryName("Category 2");
    }

    @Test
    void getAllCategories() {
       Integer pageNumber = 0;
       Integer pageSize = 10;
       String sortBy = AppConstants.SORT_CATEGORIES_BY;
       String sortOrder = AppConstants.SORT_DIR;

        Sort sort = Sort.by(sortBy).ascending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sort);
        category1.setCategoryId(1L);
        category1.setCategoryName("Category 1");

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

    @Test
    void categoryAlreadyExists() {
        CategoryDTO categoryDTO1 = new CategoryDTO();
        categoryDTO1.setCategoryId(1L);
        categoryDTO1.setCategoryName("Category 1");

        when(modelMapper.map(categoryDTO1, Category.class)).thenReturn(category1);
        when(categoryRepository.findByCategoryName(categoryDTO1.getCategoryName())).thenReturn(category1);

        APIException runtimeException = assertThrows(APIException.class, () -> {
            categoryService.createCategory(categoryDTO1);
        });

        verify(categoryRepository, times(1)).findByCategoryName(categoryDTO1.getCategoryName());

        System.out.println(" exception message " +  runtimeException.getMessage());

        assertEquals("Category already exists with same name " + category1.getCategoryName(), runtimeException.getMessage());
    }

    @Test
    void deleteExistingCategory() {
        CategoryDTO categoryDTO1 = new CategoryDTO();
        categoryDTO1.setCategoryId(1L);
        categoryDTO1.setCategoryName("Category 1");


        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        doNothing().when(categoryRepository).delete(category1); // void methods
        when(modelMapper.map(category1, CategoryDTO.class)).thenReturn(categoryDTO1);
        CategoryDTO category = categoryService.deleteCategory(1L);


        verify(categoryRepository, times(1)).findById(1L);

        assertNotNull(category);
        assertEquals(categoryDTO1, category);
    }

    @Test
    void testCheckProductName() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        Method validCategoryName =  CategoryServiceImpl.class.getDeclaredMethod("checkProductName", String.class);

        validCategoryName.setAccessible(true);
        String validName = (String) validCategoryName.invoke(categoryService, "Category1");

        assertEquals("Category1", validName);
    }

    @Test
    void testCheckProductNameIsNotValid() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method validCategoryName =  CategoryServiceImpl.class.getDeclaredMethod("checkProductName", String.class);

        validCategoryName.setAccessible(true);
        // Invoke the method with an empty string (which should trigger an exception)
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
            validCategoryName.invoke(categoryService, "");
        });

        // Now assert that the cause of the InvocationTargetException is the expected APIException
        assertTrue(exception.getCause() instanceof APIException, "Expected APIException to be thrown");

        // Optionally, check the message of the exception to ensure it's correct
        APIException cause = (APIException) exception.getCause();
        assertEquals("Product name cannot be empty", cause.getMessage());
    }
}