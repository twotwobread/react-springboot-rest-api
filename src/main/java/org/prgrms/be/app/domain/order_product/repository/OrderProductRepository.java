package org.prgrms.be.app.domain.order_product.repository;

import org.prgrms.be.app.domain.order_product.OrderProduct;

import java.util.List;
import java.util.UUID;

public interface OrderProductRepository {

    OrderProduct insert(OrderProduct orderProduct);

    UUID delete(UUID orderProductId);

    List<OrderProduct> findbyOrderId(UUID orderId);

    void clear();
}
