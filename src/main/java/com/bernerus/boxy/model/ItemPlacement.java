package com.bernerus.boxy.model;

/**
 * Represents the placement of an item in a box
 */
public record ItemPlacement(ItemSize item, int rowIndex, int startPosition) {
}