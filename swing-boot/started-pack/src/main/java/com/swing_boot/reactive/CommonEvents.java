package com.swing_boot.reactive;

public enum CommonEvents implements Events {

    OPEN,
    OPEN_RANDOM_COLOR_FRAME,
    PRINT,
    TYPE;

        @Override
        public Events getType() {
            return CommonEvents.TYPE;
        }
}