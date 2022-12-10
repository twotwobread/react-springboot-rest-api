package org.prgrms.be.app.domain.brand.repository;

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
import org.prgrms.be.app.exception.DatabaseFindException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {TestConfig.class, BrandJdbcMysqlRepository.class})
class BrandJdbcMysqlRepositoryTest {
    static EmbeddedMysql embeddedMysql = embeddedMysql();
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
        brandRepository.clear();
    }

    @Test
    @DisplayName("BrandJdbcMysqlRepository에 Brand 객체를 넣을 수 있다.")
    void brand_삽입하기() {
        // given
        Brand testBrand = Brand.create("test01", new Address("test01", "36-1", 43501));
        // when
        Brand actualBrand = brandRepository.insert(testBrand);
        // then
        assertThat(actualBrand, samePropertyValuesAs(testBrand));
    }

    @Test
    @DisplayName("brand Id를 이용해서 특정 brand를 찾을 수 있다.")
    void brand_ID로_찾기() {
        // given
        Brand testBrand = brandRepository.insert(Brand.create("test01", new Address("test01", "36-1", 43501)));
        // when
        Optional<Brand> actualBrand = brandRepository.findById(testBrand.getId());
        // then
        assertThat(actualBrand.isPresent(), is(true));
        assertThat(actualBrand.get(), samePropertyValuesAs(testBrand));
    }

    @Test
    @DisplayName("brand name를 이용해서 brand들을 찾을 수 있다.")
    void brand_이름으로_찾기() {
        // given
        Brand testBrand1 = brandRepository.insert(Brand.create("test01", new Address("test01", "36-1", 43501)));
        Brand testBrand2 = brandRepository.insert(Brand.create("test01", new Address("test02", "36-2", 43502)));
        // when
        List<Brand> brandsByName = brandRepository.findBrandsByName(testBrand1.getName());
        // then
        assertThat(brandsByName, hasSize(2));
    }

    @Test
    @DisplayName("존재하는 모든 브랜드를 찾을 수 있다.")
    void 모든_brand_찾기() {
        // given
        Brand testBrand1 = brandRepository.insert(Brand.create("test01", new Address("test01", "36-1", 43501)));
        Brand testBrand2 = brandRepository.insert(Brand.create("test02", new Address("test02", "36-2", 43502)));
        // when
        List<Brand> allBrand = brandRepository.getAllBrand();
        // then
        assertThat(allBrand, hasSize(2));
    }

    @Test
    @DisplayName("brand의 name과 address를 수정할 수 있다.")
    void brand_수정하기() {
        // given
        Brand testBrand = brandRepository.insert(Brand.create("test01", new Address("test01", "36-1", 43501)));
        // when
        testBrand.setName("lee");
        Brand updateBrand = brandRepository.update(testBrand);
        //then
        assertThat(updateBrand.getName(), is("lee"));
    }

    @Test
    @DisplayName("brand Id를 이용해서 특정 brand를 삭제할 수 있다.")
    void brand_삭제하기() {
        // given
        Brand testBrand = brandRepository.insert(Brand.create("test01", new Address("test01", "36-1", 43501)));
        //when
        UUID deleteBrandId = brandRepository.delete(testBrand.getId());
        // then
        assertThrows(DatabaseFindException.class, () -> brandRepository.findById(deleteBrandId));
    }
}