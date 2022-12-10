package org.prgrms.be.app.service;

import org.prgrms.be.app.domain.member.Member;
import org.prgrms.be.app.domain.member.repository.MemberRepository;
import org.prgrms.be.app.domain.order.Order;
import org.prgrms.be.app.domain.order.dto.OrderServiceDto;
import org.prgrms.be.app.domain.order.repository.OrderRepository;
import org.prgrms.be.app.domain.order_product.OrderProduct;
import org.prgrms.be.app.domain.order_product.repository.OrderProductRepository;
import org.prgrms.be.app.domain.product.Product;
import org.prgrms.be.app.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public OrderService(OrderRepository orderRepository, OrderProductRepository orderProductRepository, ProductRepository productRepository, MemberRepository memberRepository) {
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    public List<Order> getOrderByMemberId(UUID memberId) {
        return orderRepository.findByMemberId(memberId);
    }

    public List<OrderProduct> getOrderProductByOrderId(UUID orderId) {
        return orderProductRepository.findbyOrderId(orderId);
    }

    @Transactional
    public List<OrderProduct> createOrderByProduct(UUID memberId, List<OrderServiceDto> orderServiceDtos) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("요청한 Id의 member가 존재하지 않습니다."));
        Order order = orderRepository.insert(Order.create(member.getId(), member.getAddress()));

        List<OrderProduct> orderProducts = new ArrayList<>();
        for (OrderServiceDto orderServiceDto : orderServiceDtos) {
            Product product = productRepository.findById(orderServiceDto.getProductId())
                    .orElseThrow(() -> new NoSuchElementException("존재하지 않는 상품입니다."));
            orderProducts.add(orderProductRepository.insert(OrderProduct.create(order.getId(), product.getId(), product.getPrice(), orderServiceDto.getCount())));

            product.minusStockQuantity(orderServiceDto.getCount());
            productRepository.update(product);
        }

        return orderProducts;
    }

    @Transactional
    public UUID deleteOrder(UUID orderId) {
        orderProductRepository.findbyOrderId(orderId)
                .forEach((orderProduct -> deleteOrderProduct(orderProduct.getOrderProductId(),
                        new OrderServiceDto(orderProduct.getProductId(), orderProduct.getCount()))));
        return orderRepository.delete(orderId);
    }

    @Transactional
    public UUID deleteOrderProduct(UUID orderProductId, OrderServiceDto orderServiceDto) {
        Product product = productRepository.findById(orderServiceDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        product.plusStockQuantity(orderServiceDto.getCount());
        productRepository.update(product);
        return orderProductRepository.delete(orderProductId);
    }
}
