package com.midgard.pokerengine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.midgard.pokerengine.model.Card;
import com.midgard.pokerengine.model.HandRequest;
import com.midgard.pokerengine.model.Suit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.midgard.pokerengine.service.HandEvaluatorService;

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

    @Test
    void isStraight_ValidHand_ReturnsTrueForStraight() throws Exception {
        HandRequest request = new HandRequest();
        request.setCards(List.of(
            new Card(Suit.HEARTS, 2),
            new Card(Suit.CLUBS, 3),
            new Card(Suit.DIAMONDS, 4),
            new Card(Suit.SPADES, 5),
            new Card(Suit.HEARTS, 6)
        ));
        
        when(handEvaluatorService.isStraight(anyList())).thenReturn(true);

        mockMvc.perform(post("/api/v1/hand/isstraight")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void isStraight_InvalidHandSize_ReturnsBadRequest() throws Exception {
        HandRequest request = new HandRequest();
        request.setCards(List.of(
            new Card(Suit.HEARTS, 2),
            new Card(Suit.CLUBS, 3),
            new Card(Suit.DIAMONDS, 4),
            new Card(Suit.SPADES, 5)
        ));

        mockMvc.perform(post("/api/v1/hand/isstraight")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}