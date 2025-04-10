package com.midgard.pokerengine.service;

import com.midgard.pokerengine.model.Card;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HandEvaluatorService {

    public boolean isStraight(List<Card> cards) {
        Set<Integer> values = cards.stream()
                .map(Card::getValue)
                .collect(Collectors.toSet());

        // Handle Ace-low straight
        if (values.contains(14)) {
            values.add(1);
        }

        List<Integer> sortedValues = new ArrayList<>(values);
        Collections.sort(sortedValues);

        // Check all possible 5-card sequences
        for (int i = 0; i <= sortedValues.size() - 5; i++) {
            boolean isStraight = true;
            for (int j = i; j < i + 4; j++) {
                if (sortedValues.get(j + 1) - sortedValues.get(j) != 1) {
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