package com.ecommerce.electronicsstore.controller;

import com.ecommerce.electronicsstore.entity.Product;
import com.ecommerce.electronicsstore.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void TestCreateProductPostAPI() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Samsung Galaxy S21");
        product.setDescription("The latest smartphone from Samsung with a 5G capability.");
        product.setPrice(799.99);

        when(productService.createProduct(any(Product.class))).thenReturn(product);

        String json = "{\"name\":\"Samsung Galaxy S21\",\"description\":\"The latest smartphone from Samsung with a 5G capability.\"," +
                "\"price\":799.99}";
        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().json("{'id':1,'name':'Samsung Galaxy S21'," +
                        "'description':'The latest smartphone from Samsung with a 5G capability.'," +
                        "'price':799.99}"));

        verify(productService, times(1)).createProduct(any(Product.class));
    }
}