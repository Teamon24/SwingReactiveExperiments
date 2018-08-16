package com.swing_boot.reactive;

import java.awt.Color;

public class ChangeColorEvents implements Events {

    public static final ChangeColorEvents TYPE = new ChangeColorEvents(null);

    public final Color color;

    public ChangeColorEvents(Color color) {
        this.color = color;
    }

    public Events getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        String template = "{%s: [r=%d,g=%d,b=%d]}";
        return this.color == null ? "ChangeColorEvent.TYPE" : String.format(template, "color", this.color.getRed(), this.color.getGreen(), this.color.getBlue());
    }
}
