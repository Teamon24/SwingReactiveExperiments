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
        return this.color == null ? "{no color}" : String.format(template, "color", this.color.getRed(), this.color.getGreen(), this.color.getBlue());
    }

    public static class ClearColor extends ChangeColorEvents {

        public static final ClearColor CLEAR_COLOR = new ClearColor();

        private ClearColor() {
            super(Color.WHITE);
        }
    }
}
