package org.prgrms.be.app.domain.order;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.prgrms.be.app.domain.Address;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
public class Order {
    private final UUID id;
    private final UUID memberId;
    private final LocalDateTime createdAt;
    private Address address;
    private OrderStatus orderStatus;

    public Order(UUID id, UUID memberId, Address address, LocalDateTime createdAt, OrderStatus orderStatus) {
        this.id = id;
        this.memberId = memberId;
        this.address = address;
        this.createdAt = createdAt;
        this.orderStatus = orderStatus;
    }

    public static Order create(UUID memberId, Address address) {
        return new Order(UUID.randomUUID(), memberId, address, LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS), OrderStatus.READY);
    }
}
