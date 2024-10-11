package com.ecommerce.project.controller;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
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

    //@GetMapping("/api/public/categories")
    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories() {
        CategoryResponse categoryResponse =  categoryService.getAllCategories();
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    //@PostMapping("/api/public/categories")
    @RequestMapping(value = "/public/categories", method = RequestMethod.POST)
    public ResponseEntity<String> createCategory(@Valid @RequestBody Category category) {
        categoryService.createCategory(category);
        return new ResponseEntity<>("Category added successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
            String status = categoryService.deleteCategory(categoryId);
            //return new ResponseEntity<>(status, HttpStatus.OK);   this is the common way to send ResponseEntity to browser
            //return ResponseEntity.ok(status);
            return ResponseEntity.status(HttpStatus.OK).body(status);
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@Valid @RequestBody Category category,
                                                 @PathVariable Long categoryId) {
        Category updatedCategory = categoryService.updateCategory(category, categoryId);
        return new ResponseEntity<>("Category updated successfully with category id : " + categoryId, HttpStatus.OK);
    }


}
