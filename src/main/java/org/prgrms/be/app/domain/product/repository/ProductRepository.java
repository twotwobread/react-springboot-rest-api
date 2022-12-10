package org.prgrms.be.app.domain.product.repository;

import org.prgrms.be.app.domain.product.CategoryType;
import org.prgrms.be.app.domain.product.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product insert(Product product);

    UUID delete(UUID productId);

    Product update(Product product);

    Optional<Product> findById(UUID productId);

    List<Product> findByBrandId(UUID brandId);

    List<Product> getAllProduct();

    List<Product> findProductsByName(String name);

    List<Product> findByBrandIdAndCategory(UUID brandId, CategoryType category);

    void clear();
}
