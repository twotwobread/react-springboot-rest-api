package org.prgrms.be.app.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.be.app.domain.product.CategoryGroup;
import org.prgrms.be.app.domain.product.CategoryType;

import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.is;

class CategoryGroupTest {

    private static CategoryType selectCategoryType() {
        return CategoryType.BACKPACK;
    }

    @Test
    @DisplayName("특정 카테고리 타입을 넣으면 해당 타입이 속하는 그룹을 반환할 수 있다.")
    void category_그룹얻기() {
        CategoryType categoryType = selectCategoryType();
        CategoryGroup categoryGroup = CategoryGroup.findByCategoryType(categoryType);

        assertThat(categoryGroup.name(), is(CategoryGroup.BACK.name()));
        assertThat(categoryGroup.getTitle(), is(CategoryGroup.BACK.getTitle()));
    }
}