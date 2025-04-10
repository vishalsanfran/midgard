package com.midgard.pokerengine.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a playing card with a suit and value.
 * Values range from 2-14 (14 being Ace).
 */
@Data
@NoArgsConstructor
public class Card {
  private Suit suit;
  private int value;

  /**
   * Creates a new card with the specified suit and value.
   *
   * @param suit the card's suit
   * @param value the card's value (2-14)
   * @throws IllegalArgumentException if value is invalid
   */
  public Card(Suit suit, int value) {
    if (value < 2 || value > 14) {
      throw new IllegalArgumentException("Card value must be between 2 and 14");
    }
    this.suit = suit;
    this.value = value;
  }

  /**
   * Creates a card from a string representation (e.g., "AS" for Ace of Spades).
   *
   * @param card the string representation
   * @return new Card instance
   * @throws IllegalArgumentException if format is invalid
   */
  public static Card fromString(String card) {
    if (card == null || card.length() != 2) {
      throw new IllegalArgumentException("Card must be 2 characters");
    }
    if (!card.matches("^([2-9]|10|[JQKA])[CDHS]$")) {
      throw new IllegalArgumentException("Invalid card format: " + card);
    }

    String rankStr = card.substring(0, card.length() - 1);
    String suitSymbol = card.substring(card.length() - 1);

    int value = switch (rankStr) {
      case "J" -> 11;
      case "Q" -> 12;
      case "K" -> 13;
      case "A" -> 14;
      default -> Integer.parseInt(rankStr);
    };

    return new Card(Suit.fromSymbol(suitSymbol), value);
  }
}