package com.bernerus.boxy.api.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Article and quantity to be packed")
public class ArticleQuantityV1 {

    @Schema(
            description = "Item type. Only allowed values: ITEM_1, ITEM_2, ..., ITEM_8.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "ITEM_8"
    )
    private ItemSizeV1 item;

    @Schema(
            description = "Number of this article",
            example = "6",
            minimum = "1"
    )
    private int quantity;
}
