package com.midgard.pokerengine.controller;

import com.midgard.pokerengine.model.HandRequest;
import com.midgard.pokerengine.service.HandEvaluatorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hand")
public class HandEvaluatorController {

    private final HandEvaluatorService handEvaluatorService;

    public HandEvaluatorController(HandEvaluatorService handEvaluatorService) {
        this.handEvaluatorService = handEvaluatorService;
    }

    @PostMapping("/isstraight")
    public ResponseEntity<Boolean> isStraight(@Valid @RequestBody HandRequest request) {
        return ResponseEntity.ok(handEvaluatorService.isStraight(request.getCards()));
    }
}