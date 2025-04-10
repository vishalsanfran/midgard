package com.midgard.pokerengine.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class Card {
    @NotNull(message = "Suit is required")
    Suit suit;
    
    @NotNull(message = "Value is required")
    @Min(value = 2, message = "Card value must be between 2 and 14")
    @Max(value = 14, message = "Card value must be between 2 and 14")
    Integer value;

    @JsonCreator
    public Card(
            @JsonProperty("suit") Suit suit,
            @JsonProperty("value") Integer value) {
        this.suit = suit;
        this.value = value;
    }

    public static Card fromString(String card) {
        if (!card.matches("^([2-9]|10|[JQKA])[CDHS]$")) {
            throw new IllegalArgumentException("Invalid card format: " + card);
        }

        String rankStr = card.substring(0, card.length() - 1);
        String suitSymbol = card.substring(card.length() - 1);

        int value = switch (rankStr) {
            case "J" -> 11;
            case "Q" -> 12;
            case "K" -> 13;
            case "A" -> 14;
            default -> Integer.parseInt(rankStr);
        };

        return new Card(Suit.fromSymbol(suitSymbol), value);
    }
}