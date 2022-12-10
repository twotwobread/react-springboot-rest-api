package org.prgrms.be.app.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProductFilterDto {
    private String brandId;
    private String category;

    public ProductFilterDto(String brandId, String category) {
        this.brandId = brandId;
        this.category = category;
    }
}
