package com.ecommerce.electronicsstore.repository;

import com.ecommerce.electronicsstore.entity.Basket;
import com.ecommerce.electronicsstore.entity.BasketItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BasketRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BasketRepository basketRepository;

    @Test
    public void TestFindBasketById() {
        // given
        Basket basket = new Basket();
        basket.setTotalPrice(1000);
        entityManager.persist(basket);
        entityManager.flush();

        // when
        Basket found = basketRepository.findById(basket.getId()).get();

        // then
        assertThat(found.getId())
                .isEqualTo(basket.getId());
    }

    @Test
    public void TestSaveBasketReturnsSavedBasket() {
        // given

        Basket basket = new Basket();

        basket.setItems(Arrays.asList(BasketItem.builder()
                .quantity(1)
                .price(1000)
                .build()));

        basket.setTotalPrice(1000);

        // when
        basketRepository.save(basket);

        // then
        assertThat(basket.getId())
                .isNotNull();
    }
}
