package com.bernerus.boxy.service;

import com.bernerus.boxy.api.BadRequestException;
import com.bernerus.boxy.model.Box;
import com.bernerus.boxy.model.ItemSize;
import com.bernerus.boxy.util.BoxUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BoxService {
    //Sort boxes by width for future proofing...
    private static final List<Box> BOXES = Arrays.stream(Box.values())
            .filter(b -> b.getArea() > 0)
            .sorted(Comparator.comparing(Box::getWidth))
            .toList();

    /**
     * Calculates the smallest box that can contain all the given items
     *
     * @param items      the list of items to calculate metrics for
     * @param fillFactor the fill factor threshold for random fill attempts (0.0-1.0)
     * @return the smallest suitable box or BOX_PICKUP if no box fits
     */
    public Box calculateSmallestBoxFor(@NonNull List<ItemSize> items, double fillFactor) {
        if (items.isEmpty()) {
            throw new BadRequestException("No items provided");
        }
        
        if(items.size() >= 100) {
            return Box.BOX_PICKUP;
        }

        // No turning or twisting
        // Each item are 1 row and n columns
        // Find the smallest possible box for items

        List<ItemSize> sortedItems = items.stream()
                .sorted(Comparator.comparing(ItemSize::width).reversed())
                .toList();

        ItemSize firstItem = sortedItems.getFirst();
        int boxMinWidth = firstItem.width();

        // Remove boxes that are not wide enough
        List<Box> availableBoxes = BOXES.stream()
                .filter(b -> b.hasAtLeastWidth(boxMinWidth))
                .toList();

        // Exit early if we have no boxes that fit
        if (availableBoxes.isEmpty()) {
            return Box.BOX_PICKUP;
        }

        final Integer totalItemArea = items.stream()
                .map(ItemSize::getArea)
                .reduce(0, Integer::sum);

        // Find the first box that can contain all items
        Optional<Box> suitableBox = availableBoxes.stream()
                .filter(box -> BoxUtils.canBoxContain(box, sortedItems, totalItemArea, fillFactor))
                .findFirst();

        return suitableBox.orElse(Box.BOX_PICKUP);
    }
}
