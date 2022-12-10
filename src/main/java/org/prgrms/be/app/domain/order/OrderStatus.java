package org.prgrms.be.app.domain.order;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum OrderStatus {
    READY("배송준비"),
    START("배송시작"),
    COMPLETE("배송완료"),
    EMPTY("없음");

    private String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public static OrderStatus of(String strStatus) {
        return Arrays.stream(OrderStatus.values())
                .filter(orderStatus -> Objects.equals(orderStatus.getStatus(), strStatus))
                .findAny()
                .orElse(EMPTY);
    }
}
