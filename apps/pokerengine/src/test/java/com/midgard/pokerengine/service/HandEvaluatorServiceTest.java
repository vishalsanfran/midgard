package com.midgard.pokerengine.service;

import com.midgard.pokerengine.model.Card;
import com.midgard.pokerengine.model.Rank;
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
            new Card(Suit.HEARTS, Rank.TWO),
            new Card(Suit.CLUBS, Rank.THREE),
            new Card(Suit.DIAMONDS, Rank.FOUR),
            new Card(Suit.SPADES, Rank.FIVE),
            new Card(Suit.HEARTS, Rank.SIX)
        );
        assertTrue(handEvaluatorService.isStraight(hand));
    }

    @Test
    void isStraight_AceLowStraight_ReturnsTrue() {
        List<Card> hand = List.of(
            new Card(Suit.HEARTS, Rank.ACE), // Ace as low
            new Card(Suit.CLUBS, Rank.TWO),
            new Card(Suit.DIAMONDS, Rank.THREE),
            new Card(Suit.SPADES, Rank.FOUR),
            new Card(Suit.HEARTS, Rank.FIVE)
        );
        assertTrue(handEvaluatorService.isStraight(hand));
    }

    @Test
    void isStraight_AceHighStraight_ReturnsTrue() {
        List<Card> hand = List.of(
            new Card(Suit.HEARTS, Rank.TEN),
            new Card(Suit.CLUBS, Rank.JACK),
            new Card(Suit.DIAMONDS, Rank.QUEEN),
            new Card(Suit.SPADES, Rank.KING),
            new Card(Suit.HEARTS, Rank.ACE) // Ace as high
        );
        assertTrue(handEvaluatorService.isStraight(hand));
    }

    @Test
    void isStraight_NotAStraight_ReturnsFalse() {
        List<Card> hand = List.of(
            new Card(Suit.HEARTS, Rank.TWO),
            new Card(Suit.CLUBS, Rank.THREE),
            new Card(Suit.DIAMONDS, Rank.FOUR),
            new Card(Suit.SPADES, Rank.SIX),
            new Card(Suit.HEARTS, Rank.SEVEN)
        );
        assertFalse(handEvaluatorService.isStraight(hand));
    }

    @Test
    void isStraight_SevenCardStraight_ReturnsTrue() {
        List<Card> hand = List.of(
            new Card(Suit.HEARTS, Rank.TWO),
            new Card(Suit.CLUBS, Rank.THREE),
            new Card(Suit.DIAMONDS, Rank.FOUR),
            new Card(Suit.SPADES, Rank.FIVE),
            new Card(Suit.HEARTS, Rank.SIX),
            new Card(Suit.CLUBS, Rank.SEVEN),
            new Card(Suit.DIAMONDS, Rank.EIGHT)
        );
        assertTrue(handEvaluatorService.isStraight(hand));
    }

    @Test
    void isStraight_SevenCardWithStraight_ReturnsFalse() {
        List<Card> hand = List.of(
            new Card(Suit.HEARTS, Rank.TWO),
            new Card(Suit.CLUBS, Rank.THREE),
            new Card(Suit.DIAMONDS, Rank.FOUR),
            new Card(Suit.SPADES, Rank.FIVE),
            new Card(Suit.HEARTS, Rank.SIX),
            new Card(Suit.CLUBS, Rank.EIGHT),
            new Card(Suit.DIAMONDS, Rank.TEN)
        );
        assertFalse(handEvaluatorService.isStraight(hand));
    }

    @Test
    void isStraight_SevenCardNoStraight_ReturnsFalse() {
        List<Card> hand = List.of(
            new Card(Suit.HEARTS, Rank.TWO),
            new Card(Suit.CLUBS, Rank.FOUR),
            new Card(Suit.DIAMONDS, Rank.SIX),
            new Card(Suit.SPADES, Rank.EIGHT),
            new Card(Suit.HEARTS, Rank.TEN),
            new Card(Suit.CLUBS, Rank.QUEEN),
            new Card(Suit.DIAMONDS, Rank.ACE)
        );
        assertFalse(handEvaluatorService.isStraight(hand));
    }
}