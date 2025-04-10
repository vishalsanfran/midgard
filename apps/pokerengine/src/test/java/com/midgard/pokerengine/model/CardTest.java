package com.midgard.pokerengine.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void fromString_ValidCard_CreatesCard() {
        Card card = Card.fromString("AS");
        assertEquals(Suit.SPADES, card.getSuit());
        assertEquals(14, card.getValue());
    }

    @Test
    void fromString_InvalidFormat_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            Card.fromString("1S")
        );
    }

    @Test
    void serialize_ValidCard_CreatesValidJson() throws Exception {
        Card card = new Card(Suit.HEARTS, 10);
        String json = objectMapper.writeValueAsString(card);
        assertEquals("{\"suit\":\"HEARTS\",\"value\":10}", json);
    }

    @Test
    void deserialize_ValidJson_CreatesCard() throws Exception {
        String json = "{\"suit\":\"HEARTS\",\"value\":10}";
        Card card = objectMapper.readValue(json, Card.class);
        assertEquals(Suit.HEARTS, card.getSuit());
        assertEquals(10, card.getValue());
    }

    @Test
    void equals_SameValues_ReturnsTrue() {
        Card card1 = new Card(Suit.HEARTS, 10);
        Card card2 = new Card(Suit.HEARTS, 10);
        assertEquals(card1, card2);
        assertEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    void equals_DifferentSuit_ReturnsFalse() {
        Card card1 = new Card(Suit.HEARTS, 10);
        Card card2 = new Card(Suit.SPADES, 10);
        assertNotEquals(card1, card2);
        assertNotEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    void equals_DifferentValue_ReturnsFalse() {
        Card card1 = new Card(Suit.HEARTS, 10);
        Card card2 = new Card(Suit.HEARTS, 11);
        assertNotEquals(card1, card2);
        assertNotEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    void equals_Null_ReturnsFalse() {
        Card card = new Card(Suit.HEARTS, 10);
        assertNotEquals(card, null);
    }

    @Test
    void equals_DifferentClass_ReturnsFalse() {
        Card card = new Card(Suit.HEARTS, 10);
        assertNotEquals(card, "not a card");
    }
}