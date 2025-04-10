package com.midgard.pokerengine.model;

import lombok.Data;

import java.util.List;

/**
 * Request object for poker hand operations.
 * Contains a list of cards that make up a poker hand.
 */
@Data
public class HandRequest {
    private List<Card> cards;

    // No constructor with PokerConfig; validation will happen elsewhere
    public HandRequest(List<Card> cards) {
        this.cards = cards;
    }

    public HandRequest() {
        // Default constructor for Jackson
    }
}