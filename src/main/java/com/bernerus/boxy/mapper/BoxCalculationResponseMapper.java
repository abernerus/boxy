package com.bernerus.boxy.mapper;

import com.bernerus.boxy.api.v1.BoxSizeV1;
import com.bernerus.boxy.api.v1.CalculateBoxSizeResponseV1;
import com.bernerus.boxy.model.Box;
import org.springframework.lang.NonNull;

/**
 * Mapper for converting box calculation results to response DTOs
 */
public final class BoxCalculationResponseMapper {
    private BoxCalculationResponseMapper() {
        //Not to be instantiated
    }

    /**
     * Converts a Box model to a CalculateBoxSizeResponseV1 DTO
     *
     * @param box The box model to convert
     * @return The response DTO
     */
    public static CalculateBoxSizeResponseV1 toDto(@NonNull Box box) {
        return CalculateBoxSizeResponseV1.builder()
                .boxLabel(mapBoxToEnum(box).getLabel())
                .build();
    }

    /**
     * Maps a Box model to the corresponding BoxSizeV1 enum value
     *
     * @param box The box model to map
     * @return The corresponding BoxSizeV1 enum value
     */
    private static BoxSizeV1 mapBoxToEnum(@NonNull Box box) {
        return switch (box) {
            case BOX_1 -> BoxSizeV1.SIZE_4X5;
            case BOX_2 -> BoxSizeV1.SIZE_8X12;
            case BOX_3 -> BoxSizeV1.SIZE_12X20;
            default -> BoxSizeV1.PICKUP_REQUIRED;
        };
    }
}
