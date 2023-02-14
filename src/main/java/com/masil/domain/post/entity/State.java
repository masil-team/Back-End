package com.masil.domain.post.entity;

public enum State {
    NORMAL(true),
    DELETE(false);

    private final boolean available;

    State(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }
}
