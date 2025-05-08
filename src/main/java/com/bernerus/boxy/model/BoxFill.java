
package com.bernerus.boxy.model;

import java.util.List;

/**
 * Represents how items are placed in a box
 */
public record BoxFill(Box box, List<List<ItemPlacement>> rows) {
    /**
     * Calculates the fill percentage of the box
     */
    public double getFillPercentage() {
        int filledArea = rows.stream()
                .flatMap(List::stream)
                .mapToInt(placement -> placement.item().getArea())
                .sum();

        return (double) filledArea / box.getArea();
    }
}