package com.ecommerce.electronicsstore.controller;

import com.ecommerce.electronicsstore.entity.Product;
import com.ecommerce.electronicsstore.service.ProductService;
import org.junit.jupiter.api.DisplayName;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
@DisplayName("Product Rest API Controller Tests")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("Create New Product -> 201 response")
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


    @Test
    @DisplayName("Delete Product with valid product Id -> 204 response")
    void TestDeleteProduct()  throws Exception{
        when(productService.getProductById(1L))
                .thenReturn(
                        new Product(Long.valueOf(1),
                        "name", Double.valueOf(1000), "description"));


        doNothing().when(productService).deleteProduct(1L);

        // when
        mockMvc.perform(delete("/api/product/{id}", 1))
                .andExpect(status().isNoContent());

        // then
        verify(productService, times(1)).deleteProduct(1L);
        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    @DisplayName("Delete Product with Invalid product Id -> 404 response code")
    void TestDeleteProduct_InvalidProductId()  throws Exception{
        when(productService.getProductById(1L))
                .thenReturn(
                       null);


        doNothing().when(productService).deleteProduct(1L);

        // when
        mockMvc.perform(delete("/api/product/{id}", 1))
                .andExpect(status().isNotFound());

        // then
        verify(productService, never()).deleteProduct(1L);
        verify(productService, times(1)).getProductById(1L);
    }
}