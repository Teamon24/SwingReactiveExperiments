package com.swing_boot.reactive;

import java.awt.Color;

public class ChangeColorEvents implements Events {

    public final Color color;

    public ChangeColorEvents(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        String template = "{%s: [r=%d,g=%d,b=%d]}";
        final int red = this.color.getRed();
        final int green = this.color.getGreen();
        final int blue = this.color.getBlue();
        return String.format(template, "color", red, green, blue);
    }

    public static class ClearColor extends ChangeColorEvents {
        public static final ClearColor CLEAR_COLOR = new ClearColor();
        private ClearColor() {
            super(Color.WHITE);
        }

    }
}
