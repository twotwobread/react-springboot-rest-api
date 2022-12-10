package org.prgrms.be.app.domain.order_product.repository;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.ScriptResolver;
import com.wix.mysql.config.Charset;
import com.wix.mysql.config.MysqldConfig;
import com.wix.mysql.distribution.Version;
import org.junit.jupiter.api.*;
import org.prgrms.be.app.config.TestConfig;
import org.prgrms.be.app.domain.Address;
import org.prgrms.be.app.domain.brand.Brand;
import org.prgrms.be.app.domain.brand.repository.BrandJdbcMysqlRepository;
import org.prgrms.be.app.domain.brand.repository.BrandRepository;
import org.prgrms.be.app.domain.member.Member;
import org.prgrms.be.app.domain.member.repository.MemberJdbcMysqlRepository;
import org.prgrms.be.app.domain.member.repository.MemberRepository;
import org.prgrms.be.app.domain.order.Order;
import org.prgrms.be.app.domain.order.repository.OrderJdbcMysqlRepository;
import org.prgrms.be.app.domain.order.repository.OrderRepository;
import org.prgrms.be.app.domain.order_product.OrderProduct;
import org.prgrms.be.app.domain.product.CategoryType;
import org.prgrms.be.app.domain.product.Product;
import org.prgrms.be.app.domain.product.repository.ProductJdbcMysqlRepository;
import org.prgrms.be.app.domain.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;

@SpringBootTest(classes = {TestConfig.class, OrderProductJdbcMysqlRepository.class, OrderJdbcMysqlRepository.class, ProductJdbcMysqlRepository.class, MemberJdbcMysqlRepository.class, BrandJdbcMysqlRepository.class})
@ActiveProfiles("test")
class OrderProductJdbcMysqlRepositoryTest {

    static EmbeddedMysql embeddedMysql = embeddedMysql();
    static Address daegu = new Address("Daegu", "36-1 2층", 45617);
    static Member member;
    static Order order;
    static Brand brand;
    static Product product;
    @Autowired
    BrandRepository brandRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderProductRepository orderProductRepository;

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
    static void destroy() {
        embeddedMysql.stop();
    }

    @BeforeEach
    void init() {
        member = memberRepository.insert(Member.create("test01", daegu));
        brand = brandRepository.insert(Brand.create("test1", daegu));
        order = orderRepository.insert(Order.create(member.getId(), daegu));
        product = productRepository.insert(Product.create("product1", brand.getId(), 3000, CategoryType.BACKPACK, 50));
    }

    @AfterEach
    void reset() {
        memberRepository.clear();
        orderRepository.clear();
        brandRepository.clear();
        productRepository.clear();
        orderProductRepository.clear();
    }

    @Test
    @DisplayName("OrderProductRepository에 OrderProduct 레코드를 추가할 수 있다.")
    void order_product_추가하기() {
        OrderProduct orderProduct = OrderProduct.create(order.getId(), product.getId(), product.getPrice(), 3);
        OrderProduct actualOrderProduct = orderProductRepository.insert(orderProduct);

        assertThat(actualOrderProduct, samePropertyValuesAs(orderProduct));
    }

    @Test
    @DisplayName("orderProductId로 특정 레코드를 삭제할 수 있다.")
    void id로_레코드_삭제하기() {
        OrderProduct orderProduct = orderProductRepository.insert(OrderProduct.create(order.getId(), product.getId(), product.getPrice(), 3));

        UUID delete = orderProductRepository.delete(orderProduct.getOrderProductId());

        assertThat(delete, is(orderProduct.getOrderProductId()));
    }

    @Test
    @DisplayName("특정 Order Id로 레코드를 조회할 수 있다.")
    void orderId로_조회하기() {
        OrderProduct orderProduct1 = orderProductRepository.insert(OrderProduct.create(order.getId(), product.getId(), product.getPrice(), 3));
        OrderProduct orderProduct2 = orderProductRepository.insert(OrderProduct.create(order.getId(), product.getId(), product.getPrice(), 3));

        List<OrderProduct> orderProducts = orderProductRepository.findbyOrderId(order.getId());

        assertThat(orderProducts.size(), is(2));
    }
}