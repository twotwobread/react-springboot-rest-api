package org.prgrms.be.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.prgrms.be.app.domain.Address;
import org.prgrms.be.app.domain.member.Member;
import org.prgrms.be.app.domain.member.repository.MemberRepository;
import org.prgrms.be.app.domain.order.Order;
import org.prgrms.be.app.domain.order.repository.OrderRepository;
import org.prgrms.be.app.domain.order_product.OrderProduct;
import org.prgrms.be.app.domain.order_product.repository.OrderProductRepository;
import org.prgrms.be.app.domain.product.CategoryType;
import org.prgrms.be.app.domain.product.Product;
import org.prgrms.be.app.domain.product.repository.ProductRepository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderProductRepository orderProductRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    ProductRepository productRepository;
    @InjectMocks
    OrderService orderService;

    @Test
    void orderId로_조회하기() {
        Member member = Member.create("test01", new Address("Daegu", "36-1", 54301));
        Order order = Order.create(member.getId(), member.getAddress());
        Product product = Product.create("product1", UUID.randomUUID(), 3000, CategoryType.CROSSBACK, 300);
        List<OrderProduct> orderProducts = Arrays.asList(OrderProduct.create(order.getId(), product.getId(), product.getPrice(), 3));

        doReturn(orderProducts).when(orderProductRepository).findbyOrderId(order.getId());
        List<OrderProduct> orderProductByOrderId = orderService.getOrderProductByOrderId(order.getId());

        verify(orderProductRepository).findbyOrderId(order.getId());
        assertThat(orderProductByOrderId, hasSize(1));
    }
}