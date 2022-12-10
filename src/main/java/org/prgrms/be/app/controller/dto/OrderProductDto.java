package org.prgrms.be.app.controller.dto;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

public record OrderProductDto(
        @RequestParam("productId") UUID productId,
        @RequestParam("orderProductId") UUID orderProductId,
        @RequestParam("count") int count
) {
}
