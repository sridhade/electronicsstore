package com.ecommerce.electronicsstore.repository;

import com.ecommerce.electronicsstore.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Product Repository Unit Tests")
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Product Repository test for Find Product By Id")
    public void TestFindByProductId() {
        // given
        Product product = new Product("product1", 10.0, "description1");
        entityManager.persist(product);
        entityManager.flush();

        // when
        Product found = productRepository.findById(product.getId()).orElse(null);

        // then
        assertThat(found.getName()).isEqualTo(product.getName());
        assertThat(found.getPrice()).isEqualTo(product.getPrice());
        assertThat(found.getDescription()).isEqualTo(product.getDescription());
    }

    @Test
    @DisplayName("Product Repository test for Finding all Products")
    public void TestFindAllProducts() {
        // given
        Product product1 = new Product("product1", 10.0, "description1");
        Product product2 = new Product("product2", 20.0, "description2");
        entityManager.persist(product1);
        entityManager.persist(product2);
        entityManager.flush();

        // when
        List<Product> products = productRepository.findAll();

        // then
        assertThat(products).hasSize(2);
        assertThat(products).extracting(Product::getName).containsOnly(product1.getName(), product2.getName());
    }
}
