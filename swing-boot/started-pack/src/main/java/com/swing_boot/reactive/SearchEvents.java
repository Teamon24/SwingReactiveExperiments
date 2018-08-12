package com.swing_boot.reactive;

import com.swing_boot.reactive.Events;

public enum SearchEvents implements Events {
    SEARCH_NODE_BY_NAME,
    TYPE;

    @Override
    public Events getType() {
        return TYPE;
    }
}
