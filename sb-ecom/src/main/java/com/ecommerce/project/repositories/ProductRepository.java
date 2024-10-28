package com.ecommerce.project.repositories;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryOrderByPriceAsc(Category category);

    Page<Product> findByCategory_CategoryIdOrderByPriceAsc(Long categoryId, Pageable pageDetails);

    Page<Product> findByProductNameLikeIgnoreCase(String keyWord, Pageable pageDetails);

    Product findByProductName(String productName);
}
