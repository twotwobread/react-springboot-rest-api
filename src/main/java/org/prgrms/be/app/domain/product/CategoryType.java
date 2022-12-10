package org.prgrms.be.app.domain.product;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum CategoryType {

    SHIRTS("셔츠"),
    KNIT("니트"),
    JEANS("청바지"),
    SLACKS("슬랙스"),
    LOAFERS("로퍼"),
    SNEAKERS("스니커즈"),
    BACKPACK("백팩"),
    CROSSBACK("크로스백"),
    EMPTY("없음");

    private final String title;

    CategoryType(String title) {
        this.title = title;
    }

    public static CategoryType of(String categoryString) {
        return Arrays.stream(CategoryType.values())
                .filter(categoryType -> Objects.equals(categoryType.getTitle(), categoryString))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
    }
}
