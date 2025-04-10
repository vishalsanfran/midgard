package com.midgard.pokerengine.controller;

import com.midgard.pokerengine.model.HandRequest;
import com.midgard.pokerengine.service.HandEvaluatorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for poker hand evaluation endpoints.
 */
@RestController
@RequestMapping("/api/v1/poker")
public class HandEvaluatorController {
  private final HandEvaluatorService handEvaluatorService;

  public HandEvaluatorController(HandEvaluatorService handEvaluatorService) {
    this.handEvaluatorService = handEvaluatorService;
  }

  @PostMapping("/evaluate")
  public boolean evaluateHand(@RequestBody HandRequest request) {
    return handEvaluatorService.isStraight(request.getCards());
  }
}