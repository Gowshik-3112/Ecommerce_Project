package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    //private List<Category> categories = new ArrayList<>();
    private Long nextId = 1L;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        category.setCategoryId(nextId++);
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        List<Category> categories = categoryRepository.findAll();

        Category category = categories.stream().
                            filter((x) -> x.getCategoryId().equals(categoryId)).
                            //findFirst().orElse(null);
                            findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        //categories.remove(category);
        categoryRepository.delete(category);
        return "Category with categoryId: " + categoryId + " deleted successfully";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        List<Category> categories = categoryRepository.findAll();

        Optional<Category> optionalCategory = categories.stream().
                filter((x) -> x.getCategoryId().equals(categoryId)).findFirst();

        if(optionalCategory.isPresent()) {
            Category updatedCategory = optionalCategory.get();
            updatedCategory.setCategoryName(category.getCategoryName());
            Category savedCategory = categoryRepository.save(updatedCategory);
            return savedCategory;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
    }
}
