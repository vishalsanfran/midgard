package com.midgard.pokerengine.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for rate limiting settings.
 * Provides configuration for request rate limiting parameters.
 */
@Configuration
public class RateLimitConfig {
  @Value("${rate.limit.requests:100}")
  private int maxRequests;

  @Value("${rate.limit.duration:60}")
  private int durationSeconds;

  @Value("${rate.limit.enabled:true}")
  private boolean enabled;

  /**
   * Creates a rate limit interceptor with configured parameters.
   *
   * @return configured RateLimitInterceptor instance
   */
  @Bean
  public RateLimitInterceptor rateLimitInterceptor() {
    if (!enabled) {
      return null;
    }
    return new RateLimitInterceptor(maxRequests);
  }
}