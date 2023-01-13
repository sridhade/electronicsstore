package com.ecommerce.electronicsstore.api;

import com.ecommerce.electronicsstore.entity.Discount;
import com.ecommerce.electronicsstore.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Discount Interaction API Tests")
public class DiscountAPITest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Create Discount for a Product -> 201 response")
    public void TestCreateDiscount() {
        // Create a new product
        Product product = Product.builder().name("Sample").price(399.22).build();
        ResponseEntity<Product> response = restTemplate.postForEntity("/product", product, Product.class);

        Product createdProduct = response.getBody();

        Discount discount = Discount.builder()
                .discountCode("BOGO")
                .discountPercent(100d)
                .productId(createdProduct.getId())
                .build();

        ResponseEntity<Discount> discountResponseEntity = restTemplate.postForEntity("/discounts", discount, Discount.class);
        Discount createdDiscount = discountResponseEntity.getBody();

        // Assert that the status code is 201 (Created)
        assertEquals(HttpStatus.CREATED, discountResponseEntity.getStatusCode());

        assertTrue(createdDiscount.getDiscountCode().equalsIgnoreCase("BOGO"));
        assertTrue(createdDiscount.getDiscountPercent().equals(100d));
        assertTrue(createdDiscount.getProductId().compareTo(createdProduct.getId())==0);
    }

}
