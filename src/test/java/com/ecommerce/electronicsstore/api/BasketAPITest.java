package com.ecommerce.electronicsstore.api;

import com.ecommerce.electronicsstore.entity.Basket;
import com.ecommerce.electronicsstore.entity.Discount;
import com.ecommerce.electronicsstore.entity.Product;
import com.ecommerce.electronicsstore.model.AddBasketItemRequest;
import com.ecommerce.electronicsstore.model.AddDiscountRequest;
import com.ecommerce.electronicsstore.model.Receipt;
import com.ecommerce.electronicsstore.model.RemoveBasketItemRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Basket Interaction API Tests")
public class BasketAPITest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Add Item To a New Basket")
    public void TestAddItemToBasket_NewBasket() {

        // Create a new product
        Product product =  Product.builder().name("Sample").price(399.22).build();
        ResponseEntity<Product> productResponse = restTemplate.postForEntity("/product", product, Product.class);
        Product createdProduct = productResponse.getBody();

        // Add the product to the basket
        AddBasketItemRequest request = new AddBasketItemRequest(createdProduct.getId(), 2, 0);
        HttpEntity<AddBasketItemRequest> httpEntity = new HttpEntity<>(request);
        ResponseEntity<Basket> addResponse = restTemplate.postForEntity("/basket/add-item",request,Basket.class);

        // Assert that the status code is 200 (OK)
        assertEquals(HttpStatus.OK, addResponse.getStatusCode());

        // Assert that the basket has the expected item count
        Basket createdBasket = addResponse.getBody();
        assertEquals(1, createdBasket.getItems().size());
    }

    @Test
    @DisplayName("Add Item To an existing Basket")
    public void TestAddItemToBasket_ExistingBasket() {

        // Create a new product
        Product product =  Product.builder().name("Sample").price(399.22).build();
        ResponseEntity<Product> productResponse = restTemplate.postForEntity("/product", product, Product.class);
        Product createdProduct = productResponse.getBody();

        // Create another new product
        Product product2 =  Product.builder().name("Sample 2 ").price(33.00).build();
        ResponseEntity<Product> product2Response = restTemplate.postForEntity("/product", product2, Product.class);
        Product createdProduct2 = product2Response.getBody();

        // Add first product to the basket
        AddBasketItemRequest request = new AddBasketItemRequest(createdProduct.getId(), 2, 0);
        HttpEntity<AddBasketItemRequest> httpEntity = new HttpEntity<>(request);
        ResponseEntity<Basket> addResponse = restTemplate.postForEntity("/basket/add-item",request,Basket.class);

        // Assert that the status code is 200 (OK)
        assertEquals(HttpStatus.OK, addResponse.getStatusCode());

        // Assert that the basket has the expected item count
        Basket createdBasket = addResponse.getBody();
        assertEquals(1, createdBasket.getItems().size());

        // Add second product to created basket
        request = new AddBasketItemRequest(createdProduct2.getId(), 1, createdBasket.getId());
        addResponse = restTemplate.postForEntity("/basket/add-item",request,Basket.class);

        assertEquals(HttpStatus.OK, addResponse.getStatusCode());

        // Assert that the basket has the expected item count
        Basket updatedBasket = addResponse.getBody();
        assertEquals(2, updatedBasket.getItems().size());

    }


    @Test
    @DisplayName("Remove an Item from a basket")
    public void TestRemoveItemFromBasket() {

        // Create a new product
        Product product =  Product.builder().name("Sample").price(399.22).build();
        ResponseEntity<Product> productResponse = restTemplate.postForEntity("/product", product, Product.class);
        Product createdProduct = productResponse.getBody();

        // Create another new product
        Product product2 =  Product.builder().name("Sample 2 ").price(33.00).build();
        ResponseEntity<Product> product2Response = restTemplate.postForEntity("/product", product2, Product.class);
        Product createdProduct2 = product2Response.getBody();

        // Add first product to the basket
        AddBasketItemRequest request = new AddBasketItemRequest(createdProduct.getId(), 2, 0);
        HttpEntity<AddBasketItemRequest> httpEntity = new HttpEntity<>(request);
        ResponseEntity<Basket> addResponse = restTemplate.postForEntity("/basket/add-item",request,Basket.class);

        Basket createdBasket = addResponse.getBody();

        // Add second product to created basket
        request = new AddBasketItemRequest(createdProduct2.getId(), 1, createdBasket.getId());
        addResponse = restTemplate.postForEntity("/basket/add-item",request,Basket.class);

        Basket updatedBasket = addResponse.getBody();

        assertEquals(2, updatedBasket.getItems().size());

        RemoveBasketItemRequest removeBasketItemRequest = new RemoveBasketItemRequest(createdProduct.getId(),updatedBasket.getId());

        ResponseEntity<Basket> removeResponse =  restTemplate.postForEntity("/basket/remove-item",removeBasketItemRequest,Basket.class);
        updatedBasket = removeResponse.getBody();
        assertEquals(1, updatedBasket.getItems().size());


    }

    @Test
    @DisplayName("Calculate Receipt for a basket with products without any discounts")
    public void TestGetReceipt_NoDiscounts() {

        // Create a new product
        Product product =  Product.builder().name("Sample").price(100.00).build();
        ResponseEntity<Product> productResponse = restTemplate.postForEntity("/product", product, Product.class);
        Product createdProduct = productResponse.getBody();

        // Create another new product
        Product product2 =  Product.builder().name("Sample 2 ").price(50.00).build();
        ResponseEntity<Product> product2Response = restTemplate.postForEntity("/product", product2, Product.class);
        Product createdProduct2 = product2Response.getBody();

        // Add first product to the basket
        AddBasketItemRequest request = new AddBasketItemRequest(createdProduct.getId(), 2, 0);
        HttpEntity<AddBasketItemRequest> httpEntity = new HttpEntity<>(request);
        ResponseEntity<Basket> addResponse = restTemplate.postForEntity("/basket/add-item",request,Basket.class);

        Basket createdBasket = addResponse.getBody();

        // Add second product to created basket
        request = new AddBasketItemRequest(createdProduct2.getId(), 1, createdBasket.getId());
        addResponse = restTemplate.postForEntity("/basket/add-item",request,Basket.class);

        Basket updatedBasket = addResponse.getBody();

        // Get the product by its ID
        ResponseEntity<Receipt> receiptResponse = restTemplate.getForEntity("/basket/" + updatedBasket.getId() + "/receipt", Receipt.class);

        // Assert that the status code is 200 (OK)
        assertEquals(HttpStatus.OK, receiptResponse.getStatusCode());

        // Assert that the receipt is calculated as expected
        Receipt receipt = receiptResponse.getBody();
        assertEquals(250,receipt.getTotal());
        assertEquals(250,receipt.getTotalAfterDiscount());
        assertEquals(0.0,receipt.getTotalDiscount());

        assertEquals(2,receipt.getItems().size());

    }

    @Test
    @DisplayName("Calculate Receipt for a basket with products with discounts")
    public void TestGetReceipt_WithDiscounts() {

        // Create a new product
        Product product =  Product.builder().name("Sample").price(100.00).build();
        ResponseEntity<Product> productResponse = restTemplate.postForEntity("/product", product, Product.class);
        Product createdProduct = productResponse.getBody();

        // Create another new product
        Product product2 =  Product.builder().name("Sample 2 ").price(50.00).build();
        ResponseEntity<Product> product2Response = restTemplate.postForEntity("/product", product2, Product.class);
        Product createdProduct2 = product2Response.getBody();

        // Add a discount
        AddDiscountRequest discountRequest = AddDiscountRequest.builder()
                                                .discountCode("buy1get1")
                                                .discountPercent(100d)
                                                .productId(createdProduct.getId())
                                                .build();
        ResponseEntity<Discount> discountResponse = restTemplate.postForEntity("/discounts", discountRequest, Discount.class);

        // Add first product to the basket
        AddBasketItemRequest request = new AddBasketItemRequest(createdProduct.getId(), 2, 0);
        HttpEntity<AddBasketItemRequest> httpEntity = new HttpEntity<>(request);
        ResponseEntity<Basket> addResponse = restTemplate.postForEntity("/basket/add-item",request,Basket.class);

        Basket createdBasket = addResponse.getBody();

        // Add second product to created basket
        request = new AddBasketItemRequest(createdProduct2.getId(), 1, createdBasket.getId());
        addResponse = restTemplate.postForEntity("/basket/add-item",request,Basket.class);

        Basket updatedBasket = addResponse.getBody();

        // Get the product by its ID
        ResponseEntity<Receipt> receiptResponse = restTemplate.getForEntity("/basket/" + updatedBasket.getId() + "/receipt", Receipt.class);

        // Assert that the status code is 200 (OK)
        assertEquals(HttpStatus.OK, receiptResponse.getStatusCode());

        // Assert that the receipt is calculated as expected
        Receipt receipt = receiptResponse.getBody();
        assertEquals(250,receipt.getTotal());
        assertEquals(150,receipt.getTotalAfterDiscount());
        assertEquals(100,receipt.getTotalDiscount());

        assertEquals(2,receipt.getItems().size());

    }
}
