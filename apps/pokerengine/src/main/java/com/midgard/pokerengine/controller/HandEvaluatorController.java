package com.midgard.pokerengine.controller;

import com.midgard.pokerengine.config.PokerConfig;
import com.midgard.pokerengine.exception.BusinessException;
import com.midgard.pokerengine.model.HandRequest;
import com.midgard.pokerengine.service.HandEvaluatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for poker hand evaluation endpoints.
 */
@RestController
@RequestMapping("/api/v1/hand")
public class HandEvaluatorController {

    private static final Logger logger = LoggerFactory.getLogger(HandEvaluatorController.class);

    private final HandEvaluatorService handEvaluatorService;
    private final PokerConfig pokerConfig;

    public HandEvaluatorController(HandEvaluatorService handEvaluatorService, PokerConfig pokerConfig) {
        this.handEvaluatorService = handEvaluatorService;
        this.pokerConfig = pokerConfig;
    }

    @Operation(
        summary = "Check if cards form a straight",
        description = "Evaluates whether a given set of cards forms a poker straight. " +
                    "A straight is five consecutive cards (e.g., 2-3-4-5-6). " +
                    "Ace can be used as both high (after King) or low (before 2)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully evaluated the hand",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "true",
                    summary = "Regular straight response"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request (e.g., invalid card values, missing cards)",
            content = @Content(
                mediaType = "application/json"
            )
        )
    })
    @PostMapping("/isstraight") 
    public ResponseEntity<Boolean> isStraight(@RequestBody HandRequest handRequest) {
        logger.info("Received request: {}", handRequest);

        // Validate that the cards list is not null
        if (handRequest.getCards() == null) {
            logger.error("Cards list is null");
            throw new BusinessException("Cards list is null", HttpStatus.BAD_REQUEST);
        }

        // Validate that the hand size is valid
        if (!pokerConfig.getValidHandSizes().contains(handRequest.getCards().size())) {
            logger.error("Invalid hand size: {}. Valid sizes are: {}", handRequest.getCards().size(), pokerConfig.getValidHandSizes());
            String errorMessage = String.format("Invalid hand size: %d. Valid sizes are: %s", handRequest.getCards().size(), pokerConfig.getValidHandSizes());
            throw new BusinessException(errorMessage, HttpStatus.BAD_REQUEST);
        }

        // Evaluate if the hand is a straight
        boolean result = handEvaluatorService.isStraight(handRequest.getCards());
        return ResponseEntity.ok(result);
    }

    // Handle invalid input exceptions
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}