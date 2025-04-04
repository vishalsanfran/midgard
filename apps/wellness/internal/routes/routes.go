package routes

import (
	"github.com/gin-gonic/gin"
	"github.com/midgard/apps/wellness/internal/handlers"
	"github.com/midgard/apps/wellness/internal/middleware"
)

// SetupRoutes configures all the routes for our application
func SetupRoutes(router *gin.Engine) {
	// Add request ID middleware to all routes
	router.Use(middleware.RequestID())

	// Health check endpoint
	router.GET("/health", handlers.HealthCheck)

	// API v1 group
	v1 := router.Group("/api/v1")
	{
		// Basic endpoints
		v1.GET("/ping", handlers.Ping)
		
		// Sentiment analysis endpoint
		v1.POST("/sentiment", handlers.AnalyzeSentiment)
		
		// Topic analysis endpoint
		v1.POST("/topics", handlers.AnalyzeTopics)
		
		// Streaming endpoints
		v1.POST("/stream/submit", handlers.SubmitToStream)
		v1.GET("/stream/results", handlers.StreamResults)

		// Add more route groups here
		// users := v1.Group("/users")
		// {
		//     users.POST("/", handlers.CreateUser)
		//     users.GET("/:id", handlers.GetUser)
		// }
	}
} 