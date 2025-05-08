package com.bernerus.boxy.model;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * Represents the rows in a box and manages the available space in each row
 */
public class BoxRows {
    private final Box box;
    private final List<Integer> remainingSpaceInRows;

    /**
     * Creates a new BoxRows for the given box
     */
    public BoxRows(Box box) {
        this.box = box;
        this.remainingSpaceInRows = new ArrayList<>();
    }

    /**
     * Gets the number of rows currently in use
     */
    public int size() {
        return remainingSpaceInRows.size();
    }

    /**
     * Attempts to place an item in the box, using existing rows first,
     * then creating a new row if necessary
     *
     * @param item The item to place
     * @return true if the item was placed, false otherwise
     */
    public boolean placeItem(ItemSize item) {
        // Check if item is too wide for the box
        if (item.width() > box.getWidth()) {
            return false;
        }

        // Try to find an existing row with enough space
        OptionalInt rowIndex = findRowWithSpace(item.width());

        if (rowIndex.isPresent()) {
            // Place in existing row
            int idx = rowIndex.getAsInt();
            remainingSpaceInRows.set(idx, remainingSpaceInRows.get(idx) - item.width());
            return true;
        }

        // If no existing row has enough space, try to create a new row
        if (remainingSpaceInRows.size() < box.getHeight()) {
            // Create new row with remaining space after placing item
            remainingSpaceInRows.add(box.getWidth() - item.width());
            return true;
        }

        // Couldn't place the item
        return false;
    }

    /**
     * Finds the first row with enough space for the given width
     */
    private OptionalInt findRowWithSpace(int width) {
        return IntStream.range(0, remainingSpaceInRows.size())
                .filter(i -> remainingSpaceInRows.get(i) >= width)
                .findFirst();
    }
}
