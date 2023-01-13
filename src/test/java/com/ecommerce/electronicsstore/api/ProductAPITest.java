package com.ecommerce.electronicsstore.api;

import com.ecommerce.electronicsstore.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Product Interaction API Tests")
public class ProductAPITest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Create New Product -> 201 response")
    public void TestCreateProduct() {
        // Create a new product
        Product product = Product.builder().name("Sample").price(399.22).build();
        ResponseEntity<Product> response = restTemplate.postForEntity("/product", product, Product.class);

        // Assert that the status code is 201 (CREATED)
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Assert that the returned product has the expected name and price
        Product createdProduct = response.getBody();
        assertEquals("Sample", createdProduct.getName());
        assertEquals(399.22, createdProduct.getPrice());
    }

    @Test
    @DisplayName("Get Product with valid product Id -> 200 response")
    public void TestGetProduct() {
        // Create a new product
        Product product = Product.builder().name("Product 2").price(399.22).build();
        ResponseEntity<Product> createResponse = restTemplate.postForEntity("/product", product, Product.class);
        Product createdProduct = createResponse.getBody();

        // Get the product by its ID
        ResponseEntity<Product> getResponse = restTemplate.getForEntity("/product/" + createdProduct.getId(), Product.class);

        // Assert that the status code is 200 (OK)
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());

        // Assert that the returned product has the expected name and price
        Product retrievedProduct = getResponse.getBody();
        assertEquals("Product 2", retrievedProduct.getName());
        assertEquals(399.22, retrievedProduct.getPrice());
    }

    @Test
    @DisplayName("Delete Product with valid product Id -> no content response")
    public void TestDeleteProduct() {
        // Create a new product
        Product product = Product.builder().name("Product 2").price(399.22).build();
        ResponseEntity<Product> createResponse = restTemplate.postForEntity("/product", product, Product.class);
        Product createdProduct = createResponse.getBody();

        // Delete the product
        restTemplate.delete("/product/" + createdProduct.getId());

        ResponseEntity<Product> getResponse = restTemplate.getForEntity("/product/" + createdProduct.getId(), Product.class);

        // Assert that the status code is 404 (NOT FOUND)
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());

    }


    @Test
    @DisplayName("Delete Product with invalid product Id -> 404 (NOT FOUND)")
    public void TestDeleteProduct_WithInvalidProductId() {

        ResponseEntity<Void> response = restTemplate.exchange("/product/12345", HttpMethod.DELETE, null, Void.class);

        // Assert that the status code is 404 (NOT FOUND)
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }
}
