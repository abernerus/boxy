package com.bernerus.boxy.api.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Size of an item to be packed")
public enum ItemSizeV1 {
    ITEM_1(1, 1),
    ITEM_2(2, 1),
    ITEM_3(4, 1),
    ITEM_4(5, 1),
    ITEM_5(6, 1),
    ITEM_6(8, 1),
    ITEM_7(9, 1),
    ITEM_8(12, 1);

    @Schema(description = "Width of the item", example = "1", minimum = "1")
    private final int width;

    @Schema(description = "Height of the item (only 1 supported at this time)", example = "4", minimum = "1", maximum = "1")
    private final int height;

    ItemSizeV1(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
