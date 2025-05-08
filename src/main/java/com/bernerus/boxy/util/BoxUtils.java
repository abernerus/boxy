package com.bernerus.boxy.util;

import com.bernerus.boxy.model.Box;
import com.bernerus.boxy.model.BoxRows;
import com.bernerus.boxy.model.ItemSize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Utility class for box-related operations
 */
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
     * @param fillFactor    The fill factor threshold for random fill attempts
     * @return true if the box can contain all items, false otherwise
     */
    public static boolean canBoxContain(Box box, List<ItemSize> sortedItems, Integer totalItemArea, double fillFactor) {
        if (sortedItems.size() > box.getHeight()) {
            return false;
        }
        boolean anyItemTooWide = sortedItems.stream()
                .anyMatch(item -> item.width() > box.getWidth());
        if (anyItemTooWide) {
            return false;
        }

        // First pass: place larger items using first-fit decreasing
        BoxRows rows = new BoxRows(box);
        var unplacedItems = new ArrayList<ItemSize>();

        // First pass with larger items
        for (ItemSize item : sortedItems) {
            if (!rows.placeItem(item)) {
                unplacedItems.add(item);
            }
        }

        // If no unplaced items, we're done
        if (unplacedItems.isEmpty()) {
            return true;
        }

        // Second pass: try to fit remaining items into gaps
        boolean secondPassSuccess = unplacedItems.stream()
                .allMatch(rows::placeItem);

        // If the second pass succeeded, we're done
        if (secondPassSuccess) {
            return true;
        }

        // Try random fill if items take up less than the fill factor threshold
        if (totalItemArea <= box.getArea() * fillFactor) {
            // Try chaos/random fill up to 10 times
            for (int attempt = 0; attempt < 10; attempt++) {
                if (tryRandomFill(box, new ArrayList<>(sortedItems))) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Attempts to place items in a random order to find a valid packing
     *
     * @param box   The box to fill
     * @param items The items to place
     * @return true if all items could be placed, false otherwise
     */
    static boolean tryRandomFill(Box box, List<ItemSize> items) {
        // Create a copy of the items list to avoid modifying the original
        List<ItemSize> itemsCopy = new ArrayList<>(items);

        // Shuffle the items for random placement
        // Use a new Random instance each time to ensure different shuffling
        Collections.shuffle(itemsCopy, new Random());

        // Track the remaining space in each row
        BoxRows rows = new BoxRows(box);

        // Try to place each item
        for (ItemSize item : itemsCopy) {
            if (!rows.placeItem(item)) {
                return false;
            }
        }

        // All items placed successfully
        return true;
    }
}
