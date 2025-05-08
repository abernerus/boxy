package com.bernerus.boxy.mapper;

import com.bernerus.boxy.api.BadRequestException;
import com.bernerus.boxy.api.v1.ArticleQuantityV1;
import com.bernerus.boxy.api.v1.CalculateBoxSizeRequestV1;
import com.bernerus.boxy.api.v1.ItemSizeV1;
import com.bernerus.boxy.model.ItemSize;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemSizeMapperTest {

    @Test
    void mapsSingleItemCorrectly() {
        CalculateBoxSizeRequestV1 req = CalculateBoxSizeRequestV1.builder()
                .items(List.of(ArticleQuantityV1.builder()
                        .item(ItemSizeV1.ITEM_3)
                        .quantity(2)
                        .build()))
                .build();

        List<ItemSize> result = ItemSizeMapper.fromDto(req);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(i -> i.width() == 4 && i.height() == 1));
    }

    @Test
    void mapsMultipleItemsCorrectly() {
        CalculateBoxSizeRequestV1 req = CalculateBoxSizeRequestV1.builder()
                .items(List.of(
                        ArticleQuantityV1.builder().item(ItemSizeV1.ITEM_2).quantity(1).build(),
                        ArticleQuantityV1.builder().item(ItemSizeV1.ITEM_6).quantity(3).build()
                ))
                .build();

        List<ItemSize> result = ItemSizeMapper.fromDto(req);

        assertEquals(4, result.size());
        assertEquals(2, result.getFirst().width());
        assertEquals(1, result.getFirst().height());
        assertTrue(result.subList(1, 4).stream().allMatch(i -> i.width() == 8 && i.height() == 1));
    }


    @Test
    void throwsOnEmptyItemsList() {
        CalculateBoxSizeRequestV1 req = CalculateBoxSizeRequestV1.builder()
                .items(List.of())
                .build();

        assertThrows(BadRequestException.class, () -> ItemSizeMapper.fromDto(req));
    }

    @Test
    void throwsOnNullItemsList() {
        CalculateBoxSizeRequestV1 req = CalculateBoxSizeRequestV1.builder()
                .items(null)
                .build();

        assertThrows(BadRequestException.class, () -> ItemSizeMapper.fromDto(req));
    }

    @Test
    void throwsOnZeroQuantity() {
        CalculateBoxSizeRequestV1 req = CalculateBoxSizeRequestV1.builder()
                .items(List.of(
                        ArticleQuantityV1.builder().item(ItemSizeV1.ITEM_2).quantity(0).build()
                ))
                .build();

        assertThrows(BadRequestException.class, () -> ItemSizeMapper.fromDto(req));
    }

    @Test
    void throwsOnNegativeQuantity() {
        CalculateBoxSizeRequestV1 req = CalculateBoxSizeRequestV1.builder()
                .items(List.of(
                        ArticleQuantityV1.builder().item(ItemSizeV1.ITEM_5).quantity(-3).build()
                ))
                .build();

        assertThrows(BadRequestException.class, () -> ItemSizeMapper.fromDto(req));
    }

    @Test
    void throwsOnNullItemEnum() {
        CalculateBoxSizeRequestV1 req = CalculateBoxSizeRequestV1.builder()
                .items(List.of(
                        ArticleQuantityV1.builder().item(null).quantity(3).build()
                ))
                .build();

        assertThrows(BadRequestException.class, () -> ItemSizeMapper.fromDto(req));
    }
}
