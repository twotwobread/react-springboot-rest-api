package org.prgrms.be.app.domain.product;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public enum CategoryGroup {
    TOPS("상의", Arrays.asList(CategoryType.SHIRTS, CategoryType.KNIT)),
    BOTTOMS("하의", Arrays.asList(CategoryType.JEANS, CategoryType.SLACKS)),
    SHOES("신발", Arrays.asList(CategoryType.LOAFERS, CategoryType.SNEAKERS)),
    BACK("가방", Arrays.asList(CategoryType.BACKPACK, CategoryType.CROSSBACK)),
    EMPTY("없음", Collections.emptyList());

    private String title;
    private List<CategoryType> categoryList;

    CategoryGroup(String title, List<CategoryType> categoryList) {
        this.title = title;
        this.categoryList = categoryList;
    }

    public static CategoryGroup findByCategoryType(CategoryType categoryType) {
        return Arrays.stream(CategoryGroup.values())
                .filter(categoryGroup -> categoryGroup.hasCategoryCode(categoryType))
                .findAny()
                .orElse(EMPTY);
    }

    public boolean hasCategoryCode(CategoryType categoryType) {
        return categoryList.stream()
                .anyMatch(category -> category == categoryType);
    }
}
