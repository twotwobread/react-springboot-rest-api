package org.prgrms.be.app.controller.dto;

public record ProductDto(String brandId, String name, int price, String category, int stockQuantity) {
}
