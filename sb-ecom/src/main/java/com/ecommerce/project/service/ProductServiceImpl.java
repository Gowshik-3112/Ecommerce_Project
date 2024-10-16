package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        boolean isProductPresent = true;

        List<Product> products = category.getProducts();
        for (Product product : products) {
            if(product.getProductName().equals(productDTO.getProductName())) {
                isProductPresent = false;
                break;
            }
        }

        if(isProductPresent) {
            Product product = modelMapper.map(productDTO, Product.class);
            product.setCategory(category);
            product.setImage("default.png");
            double specialPrice = product.getPrice() -
                    ((product.getDiscount() * 0.01) * product.getPrice());
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepository.save(product);
            return modelMapper.map(savedProduct, ProductDTO.class);
        } else {
            throw new APIException("Product not found");
        }
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> product = productRepository.findAll();
        if(product.isEmpty()) {
            throw new APIException("No products found");
        }

        List<ProductDTO> productDTO = product.stream().
                map(products -> modelMapper.map(products, ProductDTO.class)).
                toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTO);
        return productResponse;
    }

    @Override
    public ProductResponse findProductsByCategory(Long categoryId) {
        /*Category category = categoryRepository.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);*/
        List<Product> products = productRepository.findByCategory_CategoryIdOrderByPriceAsc(categoryId);
        if(products.isEmpty()) {
            throw new APIException("No products found");
        }

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse getProductByKeyword(String keyWord) {
        List<Product> product = productRepository.findByProductNameLikeIgnoreCase("%" + keyWord + "%");
        if(product.isEmpty()) {
            throw new APIException("No products found");
        }

        List<ProductDTO> productDTO = product.stream().
                                      map(products -> modelMapper.map(products, ProductDTO.class)).
                                      toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTO);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long categoryId) {
        Product existingProduct = productRepository.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException("Product", "categoryId", categoryId));

        Product product = modelMapper.map(productDTO, Product.class);
        existingProduct.setProductName(product.getProductName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setDiscount(product.getDiscount());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setSpecialPrice(product.getSpecialPrice());

        Product updatedProduct = productRepository.save(existingProduct);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).
                orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        productRepository.delete(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product product = productRepository.findById(productId).
                orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        String fileName = fileService.uploadServer(path, image);

        // Updating the new file name to the product
        product.setImage(fileName);
        // Save updated product
        Product updatedProduct = productRepository.save(product);
        // return DTO after mapping product to DTO
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }
}
