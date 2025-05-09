package com.bernerus.boxy.util;

import com.bernerus.boxy.model.Box;
import com.bernerus.boxy.model.BoxRows;
import com.bernerus.boxy.model.FillSettings;
import com.bernerus.boxy.model.ItemSize;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Utility class for box-related operations
 */
@Slf4j
public class BoxUtils {

    private BoxUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Determines if a box can contain all the given items
     *
     * @param box           The box to check
     * @param sortedItems   The items to place, sorted by width (largest first)
     * @param totalItemArea The total area of all items
     * @param fillSettings  The fill settings including the fill factor threshold for random fill attempts
     * @return true if the box can contain all items, false otherwise
     */
    public static boolean canBoxContain(Box box, List<ItemSize> sortedItems, Integer totalItemArea, FillSettings fillSettings) {
        boolean anyItemTooWide = sortedItems.stream()
                .anyMatch(item -> item.width() > box.getWidth());
        if (anyItemTooWide) {
            return false;
        }

        // First pass: place larger items using first-fit decreasing
        BoxRows rows = new BoxRows(box);
        List<ItemSize> unplacedItems = sortedItems.stream()
                .filter(item -> !rows.placeItem(item))
                .toList();

        // If no unplaced items, we're done
        if (unplacedItems.isEmpty()) {
            log.info("First pass succeeded");
            return true;
        }

        // Second pass: try to fit remaining items into gaps
        boolean secondPassSuccess = unplacedItems.stream()
                .allMatch(rows::placeItem);

        // If the second pass succeeded, we're done
        if (secondPassSuccess) {
            log.info("Second pass succeeded");
            return true;
        }

        if (!fillSettings.tryRandomFill()) {
            return false;
        }

        log.info("Filling with random fill, total area: {}, box area: {}, fillSettings: {}", totalItemArea, box.getArea(), fillSettings);
        // Try random fill if items take up less than the fill factor threshold
        if (totalItemArea <= box.getArea() * fillSettings.fillFactor()) {
            // Try chaos/random fill up to 10 times
            return IntStream.rangeClosed(1, fillSettings.randomFillAttempts())
                    .mapToObj(i -> new BoxRows(box))
                    .anyMatch(boxRows -> tryRandomFill(boxRows, sortedItems));
        }
        
        log.info("Random fill failed");
        return false;
    }


    /**
     * Attempts to place items in a random order to find a valid packing
     *
     * @param rows  The box-rows to fill
     * @param items The items to place
     * @return true if all items could be placed, false otherwise
     */
    static boolean tryRandomFill(BoxRows rows, List<ItemSize> items) {
        List<ItemSize> itemsCopy = new ArrayList<>(items);

        // Shuffle the items for random placement
        // Use a new Random instance each time to ensure different shuffling
        Collections.shuffle(itemsCopy, new Random());

        // Try to place each item
        return itemsCopy.stream()
                .allMatch(rows::placeItem);
    }
}
