package com.ecommerce.electronicsstore.service;

import com.ecommerce.electronicsstore.entity.Product;
import com.ecommerce.electronicsstore.exception.ProductNotFoundException;
import com.ecommerce.electronicsstore.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {


    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product getProductById(Long id) throws ProductNotFoundException {
         return  productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found with id " + id));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    public void deleteProduct(Long id) throws ProductNotFoundException {
        Product product = getProductById(id);
        productRepository.deleteById(id);
    }
}