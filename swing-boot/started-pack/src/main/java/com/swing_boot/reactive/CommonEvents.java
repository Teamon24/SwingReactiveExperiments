package com.swing_boot.reactive;

public enum CommonEvents implements Events {

    OPEN,
    PRINT_APP_ROOT_INFO,
    TYPE, CLEAR_COLOR;

        public Events getType() {
            return CommonEvents.TYPE;
        }
}