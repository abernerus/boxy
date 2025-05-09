package com.bernerus.boxy.mapper;

import com.bernerus.boxy.api.BadRequestException;
import com.bernerus.boxy.api.v1.CalculateBoxSizeRequestV1;
import com.bernerus.boxy.api.v1.ItemSizeV1;
import com.bernerus.boxy.config.DefaultFillProperties;
import com.bernerus.boxy.model.FillSettings;
import com.bernerus.boxy.model.ItemSize;

import java.util.List;
import java.util.stream.Stream;


public class CalculateBoxSizeRequestMapper {
    private CalculateBoxSizeRequestMapper() {
        //Not to be instantiated
    }

    public static List<ItemSize> toItemSizes(CalculateBoxSizeRequestV1 request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new BadRequestException("No items provided");
        }

        return request.getItems().stream()
                .flatMap(article -> {
                    ItemSizeV1 sizeV1 = article.getItem();
                    int quantity = article.getQuantity();
                    if (sizeV1 == null) {
                        throw new BadRequestException("Item type must not be null");
                    }
                    if (quantity <= 0) {
                        throw new BadRequestException("Quantity must be positive for item: " + sizeV1);
                    }
                    return Stream.generate(() -> ItemSize.of(sizeV1.getWidth(), sizeV1.getHeight()))
                            .limit(quantity);
                })
                .toList();
    }

    public static FillSettings toFillSettings(CalculateBoxSizeRequestV1 request, DefaultFillProperties defaults) {
        double fillFactor = request.getFillFactor() != null
                ? request.getFillFactor()
                : defaults.getFillFactor();

        int extraFillAttempts = request.getExtraFillAttempts() != null
                ? request.getExtraFillAttempts()
                : defaults.getExtraAttempts();

        return FillSettings.builder()
                .fillFactor(fillFactor)
                .randomFillAttempts(extraFillAttempts)
                .build();
    }


    }
