package com.midgard.pokerengine.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HandRequestTest {

    @Test
    void isValidHandSize_FiveCards_ReturnsTrue() {
        HandRequest request = new HandRequest();
        request.setCards(List.of(
            new Card(Suit.HEARTS, 2),
            new Card(Suit.CLUBS, 3),
            new Card(Suit.DIAMONDS, 4),
            new Card(Suit.SPADES, 5),
            new Card(Suit.HEARTS, 6)
        ));
        assertTrue(request.isValidHandSize());
    }

    @Test
    void isValidHandSize_SevenCards_ReturnsTrue() {
        HandRequest request = new HandRequest();
        request.setCards(List.of(
            new Card(Suit.HEARTS, 2),
            new Card(Suit.CLUBS, 3),
            new Card(Suit.DIAMONDS, 4),
            new Card(Suit.SPADES, 5),
            new Card(Suit.HEARTS, 6),
            new Card(Suit.CLUBS, 7),
            new Card(Suit.DIAMONDS, 8)
        ));
        assertTrue(request.isValidHandSize());
    }

    @Test
    void isValidHandSize_SixCards_ReturnsFalse() {
        HandRequest request = new HandRequest();
        request.setCards(List.of(
            new Card(Suit.HEARTS, 2),
            new Card(Suit.CLUBS, 3),
            new Card(Suit.DIAMONDS, 4),
            new Card(Suit.SPADES, 5),
            new Card(Suit.HEARTS, 6),
            new Card(Suit.CLUBS, 7)
        ));
        assertFalse(request.isValidHandSize());
    }

    @Test
    void isValidHandSize_NullCards_ReturnsFalse() {
        HandRequest request = new HandRequest();
        request.setCards(null);
        assertFalse(request.isValidHandSize());
    }

    @Test
    void equals_SameCards_ReturnsTrue() {
        HandRequest request1 = new HandRequest();
        HandRequest request2 = new HandRequest();
        List<Card> cards = List.of(
            new Card(Suit.HEARTS, 2),
            new Card(Suit.CLUBS, 3),
            new Card(Suit.DIAMONDS, 4),
            new Card(Suit.SPADES, 5),
            new Card(Suit.HEARTS, 6)
        );
        request1.setCards(cards);
        request2.setCards(cards);
        
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void equals_DifferentCards_ReturnsFalse() {
        HandRequest request1 = new HandRequest();
        HandRequest request2 = new HandRequest();
        request1.setCards(List.of(
            new Card(Suit.HEARTS, 2),
            new Card(Suit.CLUBS, 3),
            new Card(Suit.DIAMONDS, 4),
            new Card(Suit.SPADES, 5),
            new Card(Suit.HEARTS, 6)
        ));
        request2.setCards(List.of(
            new Card(Suit.HEARTS, 3),
            new Card(Suit.CLUBS, 4),
            new Card(Suit.DIAMONDS, 5),
            new Card(Suit.SPADES, 6),
            new Card(Suit.HEARTS, 7)
        ));
        
        assertNotEquals(request1, request2);
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void equals_Null_ReturnsFalse() {
        HandRequest request = new HandRequest();
        request.setCards(List.of(
            new Card(Suit.HEARTS, 2),
            new Card(Suit.CLUBS, 3),
            new Card(Suit.DIAMONDS, 4),
            new Card(Suit.SPADES, 5),
            new Card(Suit.HEARTS, 6)
        ));
        
        assertNotEquals(request, null);
    }

    @Test
    void equals_DifferentClass_ReturnsFalse() {
        HandRequest request = new HandRequest();
        request.setCards(List.of(
            new Card(Suit.HEARTS, 2),
            new Card(Suit.CLUBS, 3),
            new Card(Suit.DIAMONDS, 4),
            new Card(Suit.SPADES, 5),
            new Card(Suit.HEARTS, 6)
        ));
        
        assertNotEquals(request, "not a hand request");
    }
}