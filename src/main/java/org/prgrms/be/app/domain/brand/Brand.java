package org.prgrms.be.app.domain.brand;

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
public class Brand {
    private final UUID id;
    private final LocalDateTime createdAt;
    private String name;
    private Address address;

    public Brand(UUID id, String name, Address address, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.createdAt = createdAt;
    }

    public static Brand create(String name, Address address) {
        return new Brand(UUID.randomUUID(), name, address, LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
    }
}
