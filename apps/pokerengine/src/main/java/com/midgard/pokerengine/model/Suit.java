package com.midgard.pokerengine.model;

/**
 * Represents the suit of a playing card.
 * Standard poker suits: Hearts, Diamonds, Clubs, and Spades.
 */
public enum Suit {
  HEARTS("H"),
  DIAMONDS("D"),
  CLUBS("C"),
  SPADES("S");

  private final String symbol;

  Suit(String symbol) {
    this.symbol = symbol;
  }

  public String getSymbol() {
    return symbol;
  }

  /**
   * Finds a Suit by its symbol representation.
   *
   * @param symbol the symbol to look up
   * @return the matching Suit or null if not found
   */
  public static Suit fromSymbol(String symbol) {
    for (Suit suit : values()) {
      if (suit.symbol.equals(symbol)) {
        return suit;
      }
    }
    return null;
  }
}