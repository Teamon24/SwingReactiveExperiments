package com.swing_boot.reactive;

import java.awt.Color;

public class ChangeColorEvents implements Events {

    public static ChangeColorEvents TYPE = new ChangeColorEvents(null);

    public final Color color;

    public ChangeColorEvents(Color color) {
        this.color = color;
    }

    @Override
    public Events getType() {
        return TYPE;
    }
}
