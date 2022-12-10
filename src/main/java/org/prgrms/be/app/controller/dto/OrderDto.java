package org.prgrms.be.app.controller.dto;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

public record OrderDto(
        @RequestParam("productId") UUID productId,
        @RequestParam("memberId") UUID memberId,
        @RequestParam("count") int count
) {
}
