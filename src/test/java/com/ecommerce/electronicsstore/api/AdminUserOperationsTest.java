package com.ecommerce.electronicsstore.api;

import com.ecommerce.electronicsstore.config.Constants;
import com.ecommerce.electronicsstore.entity.Discount;
import com.ecommerce.electronicsstore.entity.Product;
import com.ecommerce.electronicsstore.exception.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Admin User Operations Tests")
public class AdminUserOperationsTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @DisplayName("Create New Product as Admin user -> 201 response")
    public void TestCreateProduct() {
        // Create a new product
        Product product = Product.builder().name("Sample").price(399.22).build();
        ResponseEntity<Product> response =   testRestTemplate
                .withBasicAuth("admin","admin")
                .postForEntity("/product", product, Product.class);

        // Assert that the status code is 201 (CREATED)
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Assert that the returned product has the expected name and price
        Product createdProduct = response.getBody();
        assertEquals("Sample", createdProduct.getName());
        assertEquals(399.22, createdProduct.getPrice());
    }

    @Test
    @DisplayName("Get Product with valid product Id as Admin user -> 200 response")
    public void TestGetProduct() {
        // Create a new product
        Product product = Product.builder().name("Product 2").price(399.22).build();
        ResponseEntity<Product> createResponse =   testRestTemplate
                .withBasicAuth("admin","admin")
                .postForEntity("/product", product, Product.class);
        Product createdProduct = createResponse.getBody();

        // Get the product by its ID
        ResponseEntity<Product> getResponse =   testRestTemplate
                .withBasicAuth("admin","admin")
                .getForEntity("/product/" + createdProduct.getId(), Product.class);

        // Assert that the status code is 200 (OK)
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());

        // Assert that the returned product has the expected name and price
        Product retrievedProduct = getResponse.getBody();
        assertEquals("Product 2", retrievedProduct.getName());
        assertEquals(399.22, retrievedProduct.getPrice());
    }

    @Test
    @DisplayName("Delete Product with valid product Id as Admin user -> no content response")
    public void TestDeleteProduct() {
        // Create a new product
        Product product = Product.builder().name("Product 2").price(399.22).build();
        ResponseEntity<Product> createResponse =   testRestTemplate
                .withBasicAuth("admin","admin")
                .postForEntity("/product", product, Product.class);
        Product createdProduct = createResponse.getBody();

        // Delete the product
        testRestTemplate.withBasicAuth("admin","admin").delete("/product/" + createdProduct.getId());

        ResponseEntity<Product> getResponse =   testRestTemplate.withBasicAuth("admin","admin").getForEntity("/product/" + createdProduct.getId(), Product.class);

        // Assert that the status code is 404 (NOT FOUND)
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());

    }


    @Test
    @DisplayName("Delete Product with invalid product Id as Admin user -> 404 (NOT FOUND)")
    public void TestDeleteProduct_WithInvalidProductId() {

        ResponseEntity<Void> response =   testRestTemplate.withBasicAuth("admin","admin")
                .exchange("/product/12345", HttpMethod.DELETE, null, Void.class);

        // Assert that the status code is 404 (NOT FOUND)
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }



    @Test
    @DisplayName("Delete Product as non Admin user -> 401 response code")
    public void TestDeleteProduct_NonAdminUser() {
        // Create a new product
        Product product = Product.builder().name("Product 2").price(399.22).build();
        ResponseEntity<Product> createResponse =   testRestTemplate
                .withBasicAuth("admin","admin")
                .postForEntity("/product", product, Product.class);
        Product createdProduct = createResponse.getBody();

        ResponseEntity<Void> response =   testRestTemplate.withBasicAuth("user","user")
                .exchange("/product/" + + createdProduct.getId(), HttpMethod.DELETE, null, Void.class);

        // Assert that the status code is 401 (FORBIDDEN)
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());


    }

    @Test
    @DisplayName("Create Product as non Admin user -> 401 response code")
    public void TestCreateProduct_NonAdminUser() {
        // Create a new product
        Product product = Product.builder().name("Product 2").price(399.22).build();
        ResponseEntity<Product> createResponse =   testRestTemplate
                .withBasicAuth("user","user")
                .postForEntity("/product", product, Product.class);

        // Assert that the status code is 401 (FORBIDDEN)
        assertEquals(HttpStatus.FORBIDDEN, createResponse.getStatusCode());
    }

    @Test
    @DisplayName("Create Discount for a Product as Admin user-> 201 response")
    public void TestCreateDiscount() {
        // Create a new product
        Product product = Product.builder().name("Sample").price(399.22).build();
        ResponseEntity<Product> response = testRestTemplate.withBasicAuth("admin","admin").postForEntity("/product", product, Product.class);

        Product createdProduct = response.getBody();

        Discount discount = Discount.builder()
                .discountCode(Constants.BUY_1_GET_1)
                .discountPercent(100d)
                .productId(createdProduct.getId())
                .build();

        ResponseEntity<Discount> discountResponseEntity = testRestTemplate
                .withBasicAuth("admin","admin")
                .postForEntity("/discounts", discount, Discount.class);
        Discount createdDiscount = discountResponseEntity.getBody();

        // Assert that the status code is 201 (Created)
        assertEquals(HttpStatus.CREATED, discountResponseEntity.getStatusCode());

        assertTrue(createdDiscount.getDiscountCode().equalsIgnoreCase(Constants.BUY_1_GET_1));
        assertTrue(createdDiscount.getDiscountPercent().equals(100d));
        assertTrue(createdDiscount.getProductId().compareTo(createdProduct.getId())==0);
    }


    @Test
    @DisplayName("Create Discount for a Product as non Admin user -> 401 FORBIDDEN")
    public void TestCreateDiscount_NonAdminUser() {
        // Create a new product
        Product product = Product.builder().name("Sample").price(399.22).build();
        ResponseEntity<Product> response = testRestTemplate
                .withBasicAuth("admin","admin")
                .postForEntity("/product", product, Product.class);

        Product createdProduct = response.getBody();

        Discount discount = Discount.builder()
                .discountCode(Constants.BUY_1_GET_1)
                .discountPercent(100d)
                .productId(createdProduct.getId())
                .build();

        ResponseEntity<Discount> discountResponseEntity = testRestTemplate
                .withBasicAuth("user","user")
                .postForEntity("/discounts", discount, Discount.class);

        // Assert that the status code is 401 (FORBIDDEN)
        assertEquals(HttpStatus.FORBIDDEN, discountResponseEntity.getStatusCode());

    }

    @Test
    @DisplayName("Get Product with invalid product id -> 404 NOT_FOUND")
    void TestProductNotFound() {
        // request to a non-existing product
        ResponseEntity<ErrorResponse> response = testRestTemplate
                    .withBasicAuth("admin","admin")
                    .getForEntity("/product/999", ErrorResponse.class);

        // assert that the response has a status of NOT_FOUND
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        // assert that the error message is "Product not found"
        ErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse.getMessage()).contains("Product not found");
    }
}

