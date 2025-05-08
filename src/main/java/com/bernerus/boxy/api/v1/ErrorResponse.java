package com.bernerus.boxy.api.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard error response format for API errors
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard error response")
public class ErrorResponse {

    @Schema(
            description = "Error type/classification",
            example = "Bad Request",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String error;

    @Schema(
            description = "Detailed error message",
            example = "No items provided",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String message;
}