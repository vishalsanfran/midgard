package com.midgard.pokerengine.model;

import com.midgard.pokerengine.config.PokerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HandRequestTest {


    @Test
    void constructor_FiveCards_ValidHand() {
        HandRequest request = new HandRequest(List.of(
            new Card(Suit.HEARTS, Rank.TWO),
            new Card(Suit.CLUBS, Rank.THREE),
            new Card(Suit.DIAMONDS, Rank.FOUR),
            new Card(Suit.SPADES, Rank.FIVE),
            new Card(Suit.HEARTS, Rank.SIX)
        ));
        assertNotNull(request);
    }

    @Test
    void constructor_SevenCards_ValidHand() {
        HandRequest request = new HandRequest(List.of(
            new Card(Suit.HEARTS, Rank.TWO),
            new Card(Suit.CLUBS, Rank.THREE),
            new Card(Suit.DIAMONDS, Rank.FOUR),
            new Card(Suit.SPADES, Rank.FIVE),
            new Card(Suit.HEARTS, Rank.SIX),
            new Card(Suit.CLUBS, Rank.SEVEN),
            new Card(Suit.DIAMONDS, Rank.EIGHT)
        ));
        assertNotNull(request);
    }

    @Test
    void equals_SameCards_ReturnsTrue() {
        List<Card> cards = List.of(
            new Card(Suit.HEARTS, Rank.TWO),
            new Card(Suit.CLUBS, Rank.THREE),
            new Card(Suit.DIAMONDS, Rank.FOUR),
            new Card(Suit.SPADES, Rank.FIVE),
            new Card(Suit.HEARTS, Rank.SIX)
        );
        HandRequest request1 = new HandRequest(cards);
        HandRequest request2 = new HandRequest(cards);

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void equals_DifferentCards_ReturnsFalse() {
        HandRequest request1 = new HandRequest(List.of(
            new Card(Suit.HEARTS, Rank.TWO),
            new Card(Suit.CLUBS, Rank.THREE),
            new Card(Suit.DIAMONDS, Rank.FOUR),
            new Card(Suit.SPADES, Rank.FIVE),
            new Card(Suit.HEARTS, Rank.SIX)
        ));

        HandRequest request2 = new HandRequest(List.of(
            new Card(Suit.HEARTS, Rank.THREE),
            new Card(Suit.CLUBS, Rank.FOUR),
            new Card(Suit.DIAMONDS, Rank.FIVE),
            new Card(Suit.SPADES, Rank.SIX),
            new Card(Suit.HEARTS, Rank.SEVEN)
        ));

        assertNotEquals(request1, request2);
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void equals_Null_ReturnsFalse() {
        HandRequest request = new HandRequest(List.of(
            new Card(Suit.HEARTS, Rank.TWO),
            new Card(Suit.CLUBS, Rank.THREE),
            new Card(Suit.DIAMONDS, Rank.FOUR),
            new Card(Suit.SPADES, Rank.FIVE),
            new Card(Suit.HEARTS, Rank.SIX)
        ));

        assertNotEquals(request, null);
    }

    @Test
    void equals_DifferentClass_ReturnsFalse() {
        HandRequest request = new HandRequest(List.of(
            new Card(Suit.HEARTS, Rank.TWO),
            new Card(Suit.CLUBS, Rank.THREE),
            new Card(Suit.DIAMONDS, Rank.FOUR),
            new Card(Suit.SPADES, Rank.FIVE),
            new Card(Suit.HEARTS, Rank.SIX)
        ));

        assertNotEquals(request, "not a hand request");
    }
}