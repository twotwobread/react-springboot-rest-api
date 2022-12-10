package org.prgrms.be.app.domain.order.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderServiceDto {
    private final UUID productId;
    private final int count;

    public OrderServiceDto(UUID productId, int count) {
        this.productId = productId;
        this.count = count;
    }
}
