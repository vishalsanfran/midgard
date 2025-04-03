package middleware

import (
	"log"
	"time"

	"github.com/gin-gonic/gin"
)

// Logger is a middleware that logs request details
func Logger() gin.HandlerFunc {
	return func(c *gin.Context) {
		// Start timer
		start := time.Now()

		// Process request
		c.Next()

		// Log request details
		duration := time.Since(start)
		status := c.Writer.Status()
		
		log.Printf(
			"[%s] %s %s | %d | %v | %s",
			c.Request.Method,
			c.Request.URL.Path,
			c.ClientIP(),
			status,
			duration,
			c.Errors.String(),
		)
	}
}

// RequestID adds a unique request ID to each request
func RequestID() gin.HandlerFunc {
	return func(c *gin.Context) {
		// Generate request ID (you might want to use a proper UUID library)
		requestID := time.Now().UnixNano()
		
		// Set request ID in header
		c.Writer.Header().Set("X-Request-ID", string(requestID))
		
		c.Next()
	}
} 