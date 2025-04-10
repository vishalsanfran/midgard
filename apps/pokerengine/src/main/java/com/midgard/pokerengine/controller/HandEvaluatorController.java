package com.midgard.pokerengine.controller;

import com.midgard.pokerengine.exception.BusinessException;
import com.midgard.pokerengine.model.HandRequest;
import com.midgard.pokerengine.service.HandEvaluatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for poker hand evaluation endpoints.
 */
@RestController
@RequestMapping("/api/v1/hand")
public class HandEvaluatorController {
    private final HandEvaluatorService handEvaluatorService;

    public HandEvaluatorController(HandEvaluatorService handEvaluatorService) {
        this.handEvaluatorService = handEvaluatorService;
    }

    @PostMapping("/isstraight") 
    public ResponseEntity<Boolean> evaluateHand(@RequestBody HandRequest request) {
        if (!request.isValidHandSize()) {
            throw new BusinessException("Hand must contain exactly 5 or 7 cards", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(handEvaluatorService.isStraight(request.getCards()));
    }
}