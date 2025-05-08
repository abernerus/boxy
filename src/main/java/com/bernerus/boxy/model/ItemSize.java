package com.bernerus.boxy.model;

public record ItemSize(int width, int height) {
    /**
     * Creates a new ItemSize with the specified width and height
     *
     * @param width  The width of the item
     * @param height The height of the item
     * @return A new ItemSize instance
     */
    public static ItemSize of(int width, int height) {
        return new ItemSize(width, height);
    }

    public int getArea() {
        return width * height;
    }
}
