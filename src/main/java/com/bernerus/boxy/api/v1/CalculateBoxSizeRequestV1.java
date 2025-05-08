package com.bernerus.boxy.api.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request for calculating the optimal box size")
public class CalculateBoxSizeRequestV1 {

    @Schema(
            description = "List of items to be packed, each with an article ID and quantity. " +
                    "Article IDs: 1:1x1, 2:1x2, 3:1x4, 4:1x5, 5:1x6, 6:1x8, 7:1x9, 8:1x12",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Builder.Default
    private List<ArticleQuantityV1> items = new ArrayList<>();

    @Schema(
            description = "Optional fill factor (0.0-1.0) for random fill attempts. " +
                    "If not provided, the default value from application properties will be used.",
            example = "0.75",
            defaultValue = "0.9",
            maximum = "1.0",
            minimum = "0.1",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @DecimalMin(value = "0.1", message = "Fill factor must be at least 0.1")
    @DecimalMax(value = "1.0", message = "Fill factor must be at most 1.0")
    private Double fillFactor;
}
