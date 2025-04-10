package com.midgard.pokerengine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.midgard.pokerengine.config.PokerConfig;
import com.midgard.pokerengine.exception.BusinessException;
import com.midgard.pokerengine.model.HandRequest;
import com.midgard.pokerengine.model.Card;
import com.midgard.pokerengine.model.Rank;
import com.midgard.pokerengine.model.Suit;
import com.midgard.pokerengine.service.HandEvaluatorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HandEvaluatorController.class)
class HandEvaluatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HandEvaluatorService handEvaluatorService;

    @MockBean
    private PokerConfig pokerConfig;

    @Test
    void isStraight_ValidHand_ReturnsTrueForStraight() throws Exception {
        // Arrange
        HandRequest request = new HandRequest(List.of(
            new Card(Suit.HEARTS, Rank.TWO),
            new Card(Suit.CLUBS, Rank.THREE),
            new Card(Suit.DIAMONDS, Rank.FOUR),
            new Card(Suit.SPADES, Rank.FIVE),
            new Card(Suit.HEARTS, Rank.SIX)
        ));

        // Mock the service to return true for a straight
        when(handEvaluatorService.isStraight(anyList())).thenReturn(true);

        // Mock the valid hand sizes in PokerConfig
        when(pokerConfig.getValidHandSizes()).thenReturn(List.of(5, 7));

        // Act & Assert
        mockMvc.perform(post("/api/v1/hand/isstraight")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void isStraight_InvalidHandSize_ReturnsBadRequest() throws Exception {
        // Create an invalid HandRequest with fewer than 5 cards
        HandRequest request = new HandRequest(List.of(
            new Card(Suit.HEARTS, Rank.TWO),
            new Card(Suit.CLUBS, Rank.THREE),
            new Card(Suit.DIAMONDS, Rank.FOUR),
            new Card(Suit.SPADES, Rank.FIVE)
        ));

        // Perform the POST request and verify the response
        mockMvc.perform(post("/api/v1/hand/isstraight")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void isStraight_EmptyHand_ReturnsBadRequest() throws Exception {
        // Arrange
        HandRequest handRequest = new HandRequest(Collections.emptyList());
        when(pokerConfig.getValidHandSizes()).thenReturn(Collections.singletonList(5));

        // Act & Assert
        mockMvc.perform(post("/api/v1/hand/isstraight")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(handRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid hand size: 0. Valid sizes are: [5]"));
    }

    @Test
    void isStraight_NullHand_ReturnsBadRequest() throws Exception {
        // Create an invalid HandRequest with null cards
        HandRequest request = new HandRequest(null);

        // Perform the POST request and verify the response
        mockMvc.perform(post("/api/v1/hand/isstraight")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}