package com.midgard.pokerengine.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for health check endpoints.
 */
@RestController
@RequestMapping("/api/v1/health")
public class HealthController {
  @GetMapping
  public String healthCheck() {
    return "OK";
  }
}