package org.prgrms.be.app.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.be.app.domain.product.CategoryType;
import org.prgrms.be.app.domain.product.Product;
import org.prgrms.be.app.exception.NoStockException;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductTest {

    @Test
    @DisplayName("Product 모델을 이용해서 재고를 줄일 수 있다.")
    void 재고줄이기() {
        // given
        Product product = Product.create("product1", UUID.randomUUID(), 3000, CategoryType.CROSSBACK, 10);
        // when
        product.minusStockQuantity(2);
        // then
        assertThat(product.getStockQuantity(), is(8));
    }

    @Test
    @DisplayName("Product 모델을 이용해서 재고를 늘릴 수 있다.")
    void 재고늘리기() {
        // given
        Product product = Product.create("product1", UUID.randomUUID(), 3000, CategoryType.CROSSBACK, 10);
        // when
        product.plusStockQuantity(2);
        // then
        assertThat(product.getStockQuantity(), is(12));
    }

    @Test
    @DisplayName("가진 재고보다 많은 양의 재고를 줄일려 하면 예외가 발생한다.")
    void 재고부족() {
        // given
        Product product = Product.create("product1", UUID.randomUUID(), 3000, CategoryType.CROSSBACK, 10);
        // when
        assertThrows(NoStockException.class, () -> product.minusStockQuantity(12));
    }

    @Test
    @DisplayName("재고 존재 여부를 확인할 수 있다.")
    void 재고여부체크() {
        // given
        Product product1 = Product.create("product1", UUID.randomUUID(), 3000, CategoryType.CROSSBACK, 10);
        Product product2 = Product.create("product1", UUID.randomUUID(), 3000, CategoryType.CROSSBACK, 0);
        // when
        assertThat(product1.isNoStock(), is(false));
        assertThat(product2.isNoStock(), is(true));
    }
}
