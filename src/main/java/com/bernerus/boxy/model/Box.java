package com.bernerus.boxy.model;

import lombok.Getter;

@Getter
public enum Box {
    BOX_1(4, 5),
    BOX_2(8, 12),
    BOX_3(12, 20),
    BOX_PICKUP(0, 0);

    private final int width;
    private final int height;

    Box(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public int getArea() {
        return width * height;
    }
    
    public boolean hasAtLeastWidth(int width) {
        return this.width >= width;
    }
}
