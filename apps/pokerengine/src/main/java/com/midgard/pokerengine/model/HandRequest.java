package com.midgard.pokerengine.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request object for poker hand operations.
 * Contains a list of cards that make up a poker hand.
 */
@Data
@NoArgsConstructor
public class HandRequest {
  private List<Card> cards;

  /**
   * Validates if the hand size is either 5 or 7 cards.
   *
   * @return true if hand size is valid, false otherwise
   */
  public boolean isValidHandSize() {
    return cards != null && (cards.size() == 5 || cards.size() == 7);
  }
}