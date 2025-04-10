package com.midgard.pokerengine.model;

public enum Suit {
    CLUBS("C"),
    DIAMONDS("D"),
    HEARTS("H"),
    SPADES("S");

    private final String symbol;

    Suit(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static Suit fromSymbol(String symbol) {
        for (Suit suit : values()) {
            if (suit.symbol.equals(symbol)) {
                return suit;
            }
        }
        throw new IllegalArgumentException("Invalid suit symbol: " + symbol);
    }
}