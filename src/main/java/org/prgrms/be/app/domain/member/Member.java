package org.prgrms.be.app.domain.member;

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
public class Member {
    private final UUID id;
    private final LocalDateTime createdAt;
    private Address address;
    private String name;

    public Member(UUID id, String name, Address address, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.createdAt = createdAt;
    }

    public static Member create(String name, Address address) {
        return new Member(UUID.randomUUID(), name, address, LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
    }
}
