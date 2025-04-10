package com.midgard.pokerengine.model;

import lombok.Getter;

/**
 * Card suit enum.
 */
@Getter
public enum Suit {
  HEARTS("H"),
  DIAMONDS("D"),
  CLUBS("C"),
  SPADES("S");

  private final String symbol;

  Suit(String symbol) {
    this.symbol = symbol;
  }

}