package com.bernerus.boxy.api.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public enum BoxSizeV1 {
    @Schema(description = "Box 1 - Small box (4x5)")
    SIZE_4X5("Box nr 1", 1, 1),
    @Schema(description = "Box 2 - Medium box (8x12)")
    SIZE_8X12("Box nr 2", 1, 2),
    @Schema(description = "Box 3 - Large box (12x20)")
    SIZE_12X20("Box nr 3", 1, 4),
    PICKUP_REQUIRED("Pickup required", 0, 0);

    private final String label;
    private final int width;
    private final int height;

    BoxSizeV1(String label, int width, int height) {
        this.label = label;
        this.width = width;
        this.height = height;
    }
}
