package com.midgard.pokerengine.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * Configuration class for web-specific settings.
 */
@Configuration
public class WebConfig {
  private final RateLimitInterceptor rateLimitInterceptor;

  public WebConfig(RateLimitInterceptor rateLimitInterceptor) {
    this.rateLimitInterceptor = rateLimitInterceptor;
  }

  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(rateLimitInterceptor);
  }
}