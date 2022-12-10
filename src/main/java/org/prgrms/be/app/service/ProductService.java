package org.prgrms.be.app.service;

import org.prgrms.be.app.domain.product.CategoryType;
import org.prgrms.be.app.domain.product.Product;
import org.prgrms.be.app.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProduct() {
        return productRepository.getAllProduct();
    }

    public List<Product> findByBrandIdAndCategory(UUID brandId, CategoryType category) {
        return productRepository.findByBrandIdAndCategory(brandId, category);
    }

    public Product findById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 상품입니다."));
    }

    public List<Product> findByBrandId(UUID brandId) {
        return productRepository.findByBrandId(brandId);
    }

    public Product createProduct(String name, UUID brandId, int price, CategoryType categoryType, int stockQuantity) {
        return productRepository.insert(Product.create(name, brandId, price, categoryType, stockQuantity));
    }

    public UUID deleteProduct(UUID productId) {
        return productRepository.delete(productId);
    }
}
