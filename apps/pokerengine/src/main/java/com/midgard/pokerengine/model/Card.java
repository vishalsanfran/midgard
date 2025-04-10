package com.midgard.pokerengine.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@JsonDeserialize(using = CardDeserializer.class) // Use the custom deserializer
public class Card {

    Suit suit;
    Rank rank;

    // Constructor for manual creation
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }
}
