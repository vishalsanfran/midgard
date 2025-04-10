package com.midgard.pokerengine.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor to implement rate limiting for API requests.
 * Limits the number of requests that can be made within a time window.
 */
public class RateLimitInterceptor implements HandlerInterceptor {
  private final int maxRequests;

  public RateLimitInterceptor(int maxRequests) {
    this.maxRequests = maxRequests;
  }

  @Override
  public boolean preHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler) throws Exception {
    if (isRateLimitExceeded(request)) {
      response.sendError(HttpServletResponse.SC_TOO_MANY_REQUESTS);
      return false;
    }

    updateRateLimit(request);
    return true;
  }
}