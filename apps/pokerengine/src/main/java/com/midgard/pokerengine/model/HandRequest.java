package com.midgard.pokerengine.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class HandRequest {
    @NotEmpty(message = "Cards list cannot be empty")
    @Size(min = 5, max = 7, message = "Hand must contain 5 or 7 cards")
    @Valid
    private List<Card> cards;

    @AssertTrue(message = "Hand must contain exactly 5 or 7 cards")
    public boolean isValidHandSize() {
        return cards != null && (cards.size() == 5 || cards.size() == 7);
    }
}