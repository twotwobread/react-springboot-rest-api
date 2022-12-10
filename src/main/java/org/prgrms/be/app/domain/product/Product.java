package org.prgrms.be.app.domain.product;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.prgrms.be.app.exception.NoStockException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
public class Product {
    private final UUID id;
    private final UUID brandId;
    private final LocalDateTime createdAt;
    private String name;
    private int price;
    private CategoryType category;
    private int stockQuantity;

    public Product(UUID id, String name, UUID brandId, int price, CategoryType category, LocalDateTime createdAt, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.brandId = brandId;
        this.price = price;
        this.category = category;
        this.createdAt = createdAt;
        this.stockQuantity = stockQuantity;
    }

    public static Product create(String name, UUID brandId, int price, CategoryType category, int stockQuantity) {
        return new Product(UUID.randomUUID(), name, brandId, price, category, LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS), stockQuantity);
    }

    public void plusStockQuantity(int stock) {
        stockQuantity = stockQuantity + stock;
    }

    public void minusStockQuantity(int orderCnt) {
        if (orderCnt > stockQuantity) {
            throw new NoStockException();
        }

        stockQuantity = stockQuantity - orderCnt;
    }

    public boolean isNoStock() {
        return stockQuantity <= 0;
    }
}
