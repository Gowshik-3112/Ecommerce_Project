package com.ecommerce.project.service;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);

    ProductResponse getAllProducts();

    ProductResponse findProductsByCategory(Long categoryId);

    ProductResponse getProductByKeyword(String keyWord);

    ProductDTO updateProduct(ProductDTO productDTO, Long categoryId);

    ProductDTO deleteProduct(Long productId);
}
