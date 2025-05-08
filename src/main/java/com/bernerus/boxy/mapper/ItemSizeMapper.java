package com.bernerus.boxy.mapper;

import com.bernerus.boxy.api.BadRequestException;
import com.bernerus.boxy.api.v1.CalculateBoxSizeRequestV1;
import com.bernerus.boxy.api.v1.ItemSizeV1;
import com.bernerus.boxy.model.ItemSize;

import java.util.List;
import java.util.stream.Stream;


public class ItemSizeMapper {

    public static List<ItemSize> fromDto(CalculateBoxSizeRequestV1 request) {
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


}
