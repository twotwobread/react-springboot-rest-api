package org.prgrms.be.app.domain.order.repository;

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
import org.prgrms.be.app.domain.member.Member;
import org.prgrms.be.app.domain.member.repository.MemberJdbcMysqlRepository;
import org.prgrms.be.app.domain.member.repository.MemberRepository;
import org.prgrms.be.app.domain.order.Order;
import org.prgrms.be.app.domain.order.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@SpringBootTest(classes = {TestConfig.class, OrderJdbcMysqlRepository.class, MemberJdbcMysqlRepository.class})
@ActiveProfiles("test")
class OrderJdbcMysqlRepositoryTest {

    static EmbeddedMysql embeddedMysql = embeddedMysql();
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MemberRepository memberRepository;

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
        orderRepository.clear();
        memberRepository.clear();
    }

    @Test
    @DisplayName("OrderRepository에 Order를 삽입할 수 있다.")
    void order_삽입하기() {
        // given
        Member testMember = memberRepository.insert(Member.create("test1", new Address("test1", "16-1", 49815)));
        Order testOrder = Order.create(testMember.getId(), testMember.getAddress());
        // when
        Order actualOrder = orderRepository.insert(testOrder);
        // then
        assertThat(actualOrder, samePropertyValuesAs(testOrder));
    }

    @Test
    @DisplayName("member Id를 이용해서 해당 member가 가진 order를 찾을 수 있다.")
    void order에서_member_Id로_찾기() {
        // given
        Member testMember = memberRepository.insert(Member.create("test1", new Address("test1", "16-1", 49815)));
        Order testOrder1 = orderRepository.insert(Order.create(testMember.getId(), testMember.getAddress()));
        Order testOrder2 = orderRepository.insert(Order.create(testMember.getId(), testMember.getAddress()));
        // when
        List<Order> byMemberId = orderRepository.findByMemberId(testMember.getId());
        // then
        assertThat(byMemberId, hasSize(2));
    }

    @Test
    @DisplayName("존재하는 모든 오더를 찾을 수 있다.")
    void 모든_order_찾기() {
        // given
        Member testMember = memberRepository.insert(Member.create("test1", new Address("test1", "16-1", 49815)));
        Order testOrder1 = orderRepository.insert(Order.create(testMember.getId(), testMember.getAddress()));
        Order testOrder2 = orderRepository.insert(Order.create(testMember.getId(), testMember.getAddress()));
        // when
        List<Order> allOrder = orderRepository.getAllOrder();
        // then
        assertThat(allOrder, hasSize(2));
    }

    @Test
    @DisplayName("orderd의 status와 address를 수정할 수 있다.")
    void order_수정하기() {
        // given
        Member testMember = memberRepository.insert(Member.create("test1", new Address("test1", "16-1", 49815)));
        Order testOrder = orderRepository.insert(Order.create(testMember.getId(), testMember.getAddress()));
        // when
        testOrder.setOrderStatus(OrderStatus.COMPLETE);
        testOrder.setAddress(new Address("Daegu", "16-1", 49815));
        Order updateOrder = orderRepository.update(testOrder);
        //then
        assertThat(updateOrder.getOrderStatus(), is(OrderStatus.COMPLETE));
        assertThat(updateOrder.getAddress().getAddress(), is("Daegu"));
    }

    @Test
    @DisplayName("order Id를 이용해서 특정 order를 삭제할 수 있다.")
    void order_삭제하기() {
        // given
        Member testMember = memberRepository.insert(Member.create("test1", new Address("test1", "16-1", 49815)));
        Order testOrder = orderRepository.insert(Order.create(testMember.getId(), testMember.getAddress()));
        //when
        UUID deleteOrderId = orderRepository.delete(testOrder.getId());
        List<Order> byMemberId = orderRepository.findByMemberId(deleteOrderId);
        // then
        assertThat(byMemberId.isEmpty(), is(true));
    }
}