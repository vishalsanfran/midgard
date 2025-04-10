package com.midgard.pokerengine.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Arrays;

public class CardDeserializer extends JsonDeserializer<Card> {

    @Override
    public Card deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);

        String suitValue = node.get("suit").asText();
        String rankValue = node.get("rank").asText();

        Suit suit;
        Rank rank;

        try {
            suit = Suit.valueOf(suitValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid suit: " + suitValue + ". Valid values are: " + Arrays.toString(Suit.values()));
        }

        try {
            rank = Rank.valueOf(rankValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid rank: " + rankValue + ". Valid values are: " + Arrays.toString(Rank.values()));
        }

        return new Card(suit, rank);
    }
}