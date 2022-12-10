package org.prgrms.be.app.domain.product.repository;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.ScriptResolver;
import com.wix.mysql.config.Charset;
import com.wix.mysql.config.MysqldConfig;
import com.wix.mysql.distribution.Version;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.be.app.config.TestConfig;
import org.prgrms.be.app.domain.Address;
import org.prgrms.be.app.domain.brand.Brand;
import org.prgrms.be.app.domain.brand.repository.BrandJdbcMysqlRepository;
import org.prgrms.be.app.domain.brand.repository.BrandRepository;
import org.prgrms.be.app.domain.product.CategoryType;
import org.prgrms.be.app.domain.product.Product;
import org.prgrms.be.app.exception.DatabaseFindException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {TestConfig.class, ProductJdbcMysqlRepository.class, BrandJdbcMysqlRepository.class})
@ActiveProfiles("test")
class ProductJdbcMysqlRepositoryTest {

    static EmbeddedMysql embeddedMysql = embeddedMysql();
    @Autowired
    ProductRepository productRepository;
    @Autowired
    BrandRepository brandRepository;

    static EmbeddedMysql embeddedMysql() {
        MysqldConfig config = aMysqldConfig(Version.v5_7_latest)
                .withCharset(Charset.UTF8)
                .withPort(2215)
                .withUser("test", "test1234!")
                .withTimeZone("Asia/Seoul")
                .build();
        return anEmbeddedMysql(config)
                .addSchema("test-young_cm", ScriptResolver.classPathScripts("sql/schema.sql"))
                .start();
    }

    @AfterAll
    static void shutDown() {
        embeddedMysql.stop();
    }

    @AfterEach
    void reset() {
        productRepository.clear();
        brandRepository.clear();
    }

    @Test
    @DisplayName("ProductRepository에 Product를 삽입할 수 있다.")
    void product_삽입하기() {
        // given
        Brand testBrand = brandRepository.insert(Brand.create("test1", new Address("test1", "16-1", 49815)));
        Product testProduct = Product.create("product1", testBrand.getId(), 3000, CategoryType.KNIT, 10);
        // when
        Product actualProduct = productRepository.insert(testProduct);
        // then
        assertThat(actualProduct, samePropertyValuesAs(testProduct));
    }

    @Test
    @DisplayName("brand Id를 이용해서 해당 brand가 가진 product를 찾을 수 있다.")
    void product에서_brand_id를_이용해서_product찾기() {
        // given
        Brand testBrand = brandRepository.insert(Brand.create("test1", new Address("test1", "16-1", 49815)));
        Product testProduct1 = productRepository.insert(Product.create("product1", testBrand.getId(), 3000, CategoryType.KNIT, 10));
        Product testProduct2 = productRepository.insert(Product.create("product1", testBrand.getId(), 4000, CategoryType.KNIT, 10));
        // when
        List<Product> byBrandId = productRepository.findByBrandId(testBrand.getId());
        // then
        assertThat(byBrandId, hasSize(2));
    }

    @Test
    @DisplayName("product Id를 이용해서 특정 product를 찾을 수 있다.")
    void product_id로_찾기() {
        // given
        Brand testBrand = brandRepository.insert(Brand.create("test1", new Address("test1", "16-1", 49815)));
        Product testProduct = productRepository.insert(Product.create("product1", testBrand.getId(), 3000, CategoryType.KNIT, 10));
        // when
        Optional<Product> byId = productRepository.findById(testProduct.getId());
        // then
        assertThat(byId.isPresent(), is(true));
        assertThat(byId.get(), samePropertyValuesAs(testProduct));
    }

    @Test
    @DisplayName("존재하는 모든 상품을 찾을 수 있다.")
    void 모든_product_찾기() {
        // given
        Brand testBrand = brandRepository.insert(Brand.create("test1", new Address("test1", "16-1", 49815)));
        Product testProduct1 = productRepository.insert(Product.create("product1", testBrand.getId(), 3000, CategoryType.KNIT, 10));
        Product testProduct2 = productRepository.insert(Product.create("product1", testBrand.getId(), 4000, CategoryType.KNIT, 10));
        // when
        List<Product> allProduct = productRepository.getAllProduct();
        // then
        assertThat(allProduct, hasSize(2));
    }

    @Test
    @DisplayName("product의 카테고리와 가격을 수정할 수 있다.")
    void product_수정하기() {
        // given
        Brand testBrand = brandRepository.insert(Brand.create("test1", new Address("test1", "16-1", 49815)));
        Product testProduct = productRepository.insert(Product.create("product1", testBrand.getId(), 3000, CategoryType.KNIT, 10));
        // when
        testProduct.setPrice(5000);
        testProduct.setCategory(CategoryType.CROSSBACK);
        Product update = productRepository.update(testProduct);
        //then
        assertThat(update.getPrice(), is(5000));
        assertThat(update.getCategory(), is(CategoryType.CROSSBACK));
    }

    @Test
    @DisplayName("product Id를 이용해서 특정 product를 삭제할 수 있다.")
    void product_삭제하기() {
        // given
        Brand testBrand = brandRepository.insert(Brand.create("test1", new Address("test1", "16-1", 49815)));
        Product testProduct = productRepository.insert(Product.create("product1", testBrand.getId(), 3000, CategoryType.KNIT, 10));
        //when
        UUID delete = productRepository.delete(testProduct.getId());
        // then
        assertThrows(DatabaseFindException.class, () -> productRepository.findById(delete));
    }

    @Test
    @DisplayName("brandId만 주어져도 특정 product들을 조회할 수 있다.")
    void brandId_이용해서_products_조회하기() {
        Brand testBrand = brandRepository.insert(Brand.create("test1", new Address("test1", "16-1", 49815)));
        Product testProduct = productRepository.insert(Product.create("product1", testBrand.getId(), 3000, CategoryType.KNIT, 10));

        List<Product> byBrandIdAndCategory = productRepository.findByBrandIdAndCategory(testBrand.getId(), null);

        assertThat(byBrandIdAndCategory, hasSize(1));
    }

    @Test
    @DisplayName("brandId만 주어져도 특정 product들을 조회할 수 있다.")
    void brandId_category_없는경우_products_조회하기() {
        Brand testBrand = brandRepository.insert(Brand.create("test1", new Address("test1", "16-1", 49815)));
        Product testProduct = productRepository.insert(Product.create("product1", testBrand.getId(), 3000, CategoryType.KNIT, 10));

        List<Product> byBrandIdAndCategory = productRepository.findByBrandIdAndCategory(null, null);

        assertThat(byBrandIdAndCategory, hasSize(1));
    }

    @Test
    @DisplayName("category만 주어져도 특정 product들을 조회할 수 있다.")
    void category_이용해서_products_조회하기() {
        Brand testBrand = brandRepository.insert(Brand.create("test1", new Address("test1", "16-1", 49815)));
        Product testProduct = productRepository.insert(Product.create("product1", testBrand.getId(), 3000, CategoryType.KNIT, 10));

        List<Product> byBrandIdAndCategory = productRepository.findByBrandIdAndCategory(null, CategoryType.KNIT);

        assertThat(byBrandIdAndCategory, hasSize(1));
    }
}