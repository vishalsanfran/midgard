package com.midgard.pokerengine.service;

import com.midgard.pokerengine.model.Card;
import com.midgard.pokerengine.model.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HandEvaluatorServiceTest {

    private HandEvaluatorService handEvaluatorService;

    @BeforeEach
    void setUp() {
        handEvaluatorService = new HandEvaluatorService();
    }

    @Test
    void isStraight_RegularStraight_ReturnsTrue() {
        List<Card> hand = List.of(
            new Card(Suit.HEARTS, 2),
            new Card(Suit.CLUBS, 3),
            new Card(Suit.DIAMONDS, 4),
            new Card(Suit.SPADES, 5),
            new Card(Suit.HEARTS, 6)
        );
        assertTrue(handEvaluatorService.isStraight(hand));
    }

    @Test
    void isStraight_AceLowStraight_ReturnsTrue() {
        List<Card> hand = List.of(
            new Card(Suit.HEARTS, 14), // Ace
            new Card(Suit.CLUBS, 2),
            new Card(Suit.DIAMONDS, 3),
            new Card(Suit.SPADES, 4),
            new Card(Suit.HEARTS, 5)
        );
        assertTrue(handEvaluatorService.isStraight(hand));
    }

    @Test
    void isStraight_AceHighStraight_ReturnsTrue() {
        List<Card> hand = List.of(
            new Card(Suit.HEARTS, 10),
            new Card(Suit.CLUBS, 11),
            new Card(Suit.DIAMONDS, 12),
            new Card(Suit.SPADES, 13),
            new Card(Suit.HEARTS, 14) // Ace
        );
        assertTrue(handEvaluatorService.isStraight(hand));
    }

    @Test
    void isStraight_NotAStraight_ReturnsFalse() {
        List<Card> hand = List.of(
            new Card(Suit.HEARTS, 2),
            new Card(Suit.CLUBS, 3),
            new Card(Suit.DIAMONDS, 4),
            new Card(Suit.SPADES, 6),
            new Card(Suit.HEARTS, 7)
        );
        assertFalse(handEvaluatorService.isStraight(hand));
    }

    @Test
    void isStraight_SevenCardStraight_ReturnsTrue() {
        List<Card> hand = List.of(
            new Card(Suit.HEARTS, 2),
            new Card(Suit.CLUBS, 3),
            new Card(Suit.DIAMONDS, 4),
            new Card(Suit.SPADES, 5),
            new Card(Suit.HEARTS, 6),
            new Card(Suit.CLUBS, 7),
            new Card(Suit.DIAMONDS, 8)
        );
        assertTrue(handEvaluatorService.isStraight(hand));
    }

    @Test
    void isStraight_SevenCardWithStraight_ReturnsFalse() {
        List<Card> hand = List.of(
            new Card(Suit.HEARTS, 2),
            new Card(Suit.CLUBS, 3),
            new Card(Suit.DIAMONDS, 4),
            new Card(Suit.SPADES, 5),
            new Card(Suit.HEARTS, 6),
            new Card(Suit.CLUBS, 8),
            new Card(Suit.DIAMONDS, 10)
        );
        assertFalse(handEvaluatorService.isStraight(hand));
    }

    @Test
    void isStraight_SevenCardNoStraight_ReturnsFalse() {
        List<Card> hand = List.of(
            new Card(Suit.HEARTS, 2),
            new Card(Suit.CLUBS, 4),
            new Card(Suit.DIAMONDS, 6),
            new Card(Suit.SPADES, 8),
            new Card(Suit.HEARTS, 10),
            new Card(Suit.CLUBS, 12),
            new Card(Suit.DIAMONDS, 14)
        );
        assertFalse(handEvaluatorService.isStraight(hand));
    }
}