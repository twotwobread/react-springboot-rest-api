package org.prgrms.be.app.domain.order.repository;

import org.prgrms.be.app.domain.order.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order insert(Order order);

    UUID delete(UUID orderId);

    Order update(Order order);

    List<Order> findByMemberId(UUID memberId);

    Optional<Order> findById(UUID orderId);

    List<Order> getAllOrder();

    void clear();
}
