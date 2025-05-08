package com.bernerus.boxy.controller;

import com.bernerus.boxy.api.v1.CalculateBoxSizeRequestV1;
import com.bernerus.boxy.api.v1.CalculateBoxSizeResponseV1;
import com.bernerus.boxy.api.v1.Endpoints;
import com.bernerus.boxy.config.BoxyProperties;
import com.bernerus.boxy.mapper.BoxCalculationResponseMapper;
import com.bernerus.boxy.mapper.ItemSizeMapper;
import com.bernerus.boxy.model.Box;
import com.bernerus.boxy.model.ItemSize;
import com.bernerus.boxy.service.BoxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoxController {

    private final BoxService boxService;
    private final BoxyProperties boxyProperties;

    @Operation(
            summary = "Calculate the optimal box size for a set of items",
            description = "Takes a list of item sizes and determines the smallest box that can contain them all"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Box size calculated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CalculateBoxSizeResponseV1.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters",
                    content = @Content
            )
    })
    @PostMapping(Endpoints.Box.CALCULATE_BOX_SIZE_SUB_PATH)
    public ResponseEntity<CalculateBoxSizeResponseV1> calculateBoxSize(@RequestBody CalculateBoxSizeRequestV1 request) {
        double fillFactor = request.getFillFactor() != null
                ? request.getFillFactor()
                : boxyProperties.getDefaultFillFactor();

        // Use the service to process items and return the result
        List<ItemSize> items = ItemSizeMapper.fromDto(request);
        Box box = boxService.calculateSmallestBoxFor(items, fillFactor);

        // Use the mapper to convert the result
        final CalculateBoxSizeResponseV1 response = BoxCalculationResponseMapper.toDto(box);

        return ResponseEntity.ok(response);
    }
}
