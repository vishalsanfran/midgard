package com.midgard.pokerengine.service;

import com.midgard.pokerengine.model.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
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
  
    Set<Integer> values = new HashSet<>();
    boolean hasAce = false;
    for (Card card : cards) {
        int value = card.getValue();
        if (value == 14) hasAce = true;
        values.add(value);
    }
    if (hasConsecutive(values)) {
        return true;
    }
    if (hasAce) {
        values.remove(14);
        values.add(1);
        return hasConsecutive(values);
    }
    return false;
  }

  private boolean hasConsecutive(Set<Integer> values) {
    int cur = Integer.MAX_VALUE;
    for (int value : values) {
        cur = Math.min(cur, value);
    }
    System.out.println("vkk total cards " + Integer.toString(values.size()));
    for(int i = 0; i < values.size() - 1; i++) {
      if(!values.contains(cur + 1)) {
        return false;
      }
      cur += 1;
    }
    return true;
  }
}