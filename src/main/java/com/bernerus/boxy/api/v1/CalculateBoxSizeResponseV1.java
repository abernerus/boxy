
package com.bernerus.boxy.api.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response containing the calculated box size")
public class CalculateBoxSizeResponseV1 {
    @Schema(
            description = "The optimal box size that can contain all items",
            requiredMode = RequiredMode.REQUIRED
    )
    private String boxLabel;
}
