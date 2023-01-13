package com.ecommerce.electronicsstore.service;

import com.ecommerce.electronicsstore.entity.Product;
import com.ecommerce.electronicsstore.repository.ProductRepository;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service Unit Tests")
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    @DisplayName("Product service test for create product")
    public void TestCreateProduct() {
        // given
        Product product = new Product("product1", 10.0, "description1");

        // when
        productService.createProduct(product);

        // then
        verify(productRepository,times(1)).save(product);

    }
}