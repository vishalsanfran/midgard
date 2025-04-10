package com.midgard.pokerengine.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void serialize_ValidCard_CreatesValidJson() throws Exception {
        Card card = new Card(Suit.HEARTS, Rank.TEN);
        String json = objectMapper.writeValueAsString(card);
        assertEquals("{\"suit\":\"HEARTS\",\"rank\":\"TEN\"}", json);
    }

    @Test
    void deserialize_ValidJson_CreatesCard() throws Exception {
        String json = "{\"suit\":\"HEARTS\",\"rank\":\"TEN\"}";
        Card card = objectMapper.readValue(json, Card.class);
        assertEquals(Suit.HEARTS, card.getSuit());
        assertEquals(Rank.TEN, card.getRank());
    }

    @Test
    void equals_SameValues_ReturnsTrue() {
        Card card1 = new Card(Suit.HEARTS, Rank.TEN);
        Card card2 = new Card(Suit.HEARTS, Rank.TEN);
        assertEquals(card1, card2);
        assertEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    void equals_DifferentSuit_ReturnsFalse() {
        Card card1 = new Card(Suit.HEARTS, Rank.TEN);
        Card card2 = new Card(Suit.SPADES, Rank.TEN);
        assertNotEquals(card1, card2);
        assertNotEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    void equals_DifferentRank_ReturnsFalse() {
        Card card1 = new Card(Suit.HEARTS, Rank.TEN);
        Card card2 = new Card(Suit.HEARTS, Rank.JACK);
        assertNotEquals(card1, card2);
        assertNotEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    void equals_Null_ReturnsFalse() {
        Card card = new Card(Suit.HEARTS, Rank.TEN);
        assertNotEquals(card, null);
    }

    @Test
    void equals_DifferentClass_ReturnsFalse() {
        Card card = new Card(Suit.HEARTS, Rank.TEN);
        assertNotEquals(card, "not a card");
    }
}