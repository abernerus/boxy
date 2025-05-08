package com.bernerus.boxy.util;

import com.bernerus.boxy.api.v1.ItemSizeV1;
import com.bernerus.boxy.model.Box;
import com.bernerus.boxy.model.ItemSize;
import lombok.Builder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoxUtilsTest {

    @Builder
    record BoxContainmentTestCase(String name, Box box, List<ItemSize> items, double fillFactor,
                                  boolean expectedResult) {
    }

    // Helper to convert enum to ItemSize
    private static ItemSize fromEnum(ItemSizeV1 v1) {
        return ItemSize.of(v1.getWidth(), v1.getHeight());
    }

    // Method source for canBoxContain tests
    static Stream<BoxContainmentTestCase> canBoxContainTestCases() {
        return Stream.of(
                BoxContainmentTestCase.builder()
                        .name("Empty box should contain no items")
                        .box(Box.BOX_1)
                        .items(List.of())
                        .fillFactor(0.9)
                        .expectedResult(true)
                        .build(),

                BoxContainmentTestCase.builder()
                        .name("Single item smaller than box should fit")
                        .box(Box.BOX_1)
                        .items(List.of(fromEnum(ItemSizeV1.ITEM_3))) // 4x1
                        .fillFactor(0.9)
                        .expectedResult(true)
                        .build(),

                BoxContainmentTestCase.builder()
                        .name("Single item wider than box should not fit")
                        .box(Box.BOX_1)
                        .items(List.of(fromEnum(ItemSizeV1.ITEM_8))) // 12x1 (too wide for BOX_1)
                        .fillFactor(0.9)
                        .expectedResult(false)
                        .build(),

                BoxContainmentTestCase.builder()
                        .name("Multiple items that fit in one row should fit")
                        .box(Box.BOX_1)
                        .items(List.of(
                                fromEnum(ItemSizeV1.ITEM_1),
                                fromEnum(ItemSizeV1.ITEM_1),
                                fromEnum(ItemSizeV1.ITEM_1)
                        ))
                        .fillFactor(0.9)
                        .expectedResult(true)
                        .build(),

                BoxContainmentTestCase.builder()
                        .name("Items that require multiple rows should fit if height sufficient")
                        .box(Box.BOX_2)
                        .items(List.of(
                                fromEnum(ItemSizeV1.ITEM_6), // 8x1
                                fromEnum(ItemSizeV1.ITEM_6),
                                fromEnum(ItemSizeV1.ITEM_6)
                        ))
                        .fillFactor(0.9)
                        .expectedResult(true)
                        .build(),

                BoxContainmentTestCase.builder()
                        .name("Items that require more rows than box height should not fit")
                        .box(Box.BOX_1)
                        .items(List.of(
                                fromEnum(ItemSizeV1.ITEM_4), // 5x1
                                fromEnum(ItemSizeV1.ITEM_4),
                                fromEnum(ItemSizeV1.ITEM_4),
                                fromEnum(ItemSizeV1.ITEM_4),
                                fromEnum(ItemSizeV1.ITEM_4),
                                fromEnum(ItemSizeV1.ITEM_4)
                        ))
                        .fillFactor(0.9)
                        .expectedResult(false)
                        .build(),

                BoxContainmentTestCase.builder()
                        .name("Items that fit with gap filling should fit")
                        .box(Box.BOX_1)
                        .items(List.of(
                                fromEnum(ItemSizeV1.ITEM_2), // 2x1
                                fromEnum(ItemSizeV1.ITEM_2),
                                fromEnum(ItemSizeV1.ITEM_1),
                                fromEnum(ItemSizeV1.ITEM_1)
                        ))
                        .fillFactor(0.9)
                        .expectedResult(true)
                        .build(),

                BoxContainmentTestCase.builder()
                        .name("Items with total area > box area should not fit")
                        .box(Box.BOX_1)
                        .items(List.of(
                                fromEnum(ItemSizeV1.ITEM_3), // 4x1
                                fromEnum(ItemSizeV1.ITEM_3),
                                fromEnum(ItemSizeV1.ITEM_3),
                                fromEnum(ItemSizeV1.ITEM_3),
                                fromEnum(ItemSizeV1.ITEM_3),
                                fromEnum(ItemSizeV1.ITEM_3),
                                fromEnum(ItemSizeV1.ITEM_3)
                        ))
                        .fillFactor(0.9)
                        .expectedResult(false)
                        .build()
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("canBoxContainTestCases")
    @DisplayName("Test if a box can contain items")
    void canBoxContain(BoxContainmentTestCase testCase) {
        int totalItemArea = testCase.items.stream()
                .mapToInt(ItemSize::getArea)
                .sum();

        List<ItemSize> sortedItems = new ArrayList<>(testCase.items);
        sortedItems.sort((a, b) -> Integer.compare(b.width(), a.width()));

        boolean result = BoxUtils.canBoxContain(
                testCase.box,
                sortedItems,
                totalItemArea,
                testCase.fillFactor
        );

        assertEquals(testCase.expectedResult, result,
                "Box containment test failed for: " + testCase.name);
    }
}
