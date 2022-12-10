package org.prgrms.be.app.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Address {
    private final String address;
    private final String details;
    private final int zipCode;

    public Address(String address, String details, int zipCode) {
        this.address = address;
        this.details = details;
        this.zipCode = zipCode;
    }
}
