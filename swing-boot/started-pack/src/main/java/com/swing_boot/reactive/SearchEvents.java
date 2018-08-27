package com.swing_boot.reactive;


public enum SearchEvents implements Events {
    SEARCH_NODE_BY_NAME,
    TYPE;

    public Events getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return "SearchEvents{}";
    }
}
