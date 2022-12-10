package org.prgrms.be.app.domain.order_product;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
public class OrderProduct {
    private final UUID orderProductId;
    private final UUID orderId;
    private final UUID productId;
    private final int orderPrice;
    private final int count;

    public OrderProduct(UUID orderProductId, UUID orderId, UUID productId, int orderPrice, int count) {
        this.orderId = orderId;
        this.orderProductId = orderProductId;
        this.productId = productId;
        this.orderPrice = orderPrice;
        this.count = count;
    }

    public static OrderProduct create(UUID orderId, UUID productId, int orderPrice, int count) {
        return new OrderProduct(UUID.randomUUID(), orderId, productId, orderPrice, count);
    }

    public int total() {
        return count * orderPrice;
    }
}
