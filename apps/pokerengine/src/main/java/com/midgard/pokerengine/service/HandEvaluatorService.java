package com.midgard.pokerengine.service;

import com.midgard.pokerengine.model.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Service for evaluating poker hands.
 */
@Service
public class HandEvaluatorService {
  /**
   * Checks if the given list of cards forms a straight.
   *
   * @param cards the list of cards to evaluate
   * @return true if the cards form a straight, false otherwise
   */
  public boolean isStraight(List<Card> cards) {
    if (cards == null || cards.size() < 5) {
      return false;
    }

    List<Integer> values = new ArrayList<>();
    for (Card card : cards) {
      values.add(card.getValue());
    }
    Collections.sort(values);

    for (int i = 0; i <= values.size() - 5; i++) {
      boolean isStraight = true;
      for (int j = i; j < i + 4; j++) {
        if (values.get(j + 1) - values.get(j) != 1) {
          isStraight = false;
          break;
        }
      }
      if (isStraight) {
        return true;
      }
    }
    return false;
  }
}