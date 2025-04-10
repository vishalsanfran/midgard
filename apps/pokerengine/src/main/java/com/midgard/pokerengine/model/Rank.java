package com.midgard.pokerengine.model;

import java.util.Arrays;

public enum Rank {
    TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),
    JACK(11), QUEEN(12), KING(13), ACE(14);

    private final int value;

    Rank(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Rank fromValue(int value) {
        return Arrays.stream(values())
                .filter(rank -> rank.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid rank value: " + value));
    }
}