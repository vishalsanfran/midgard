package main

import (
	"log"
	"os"
	"os/signal"
	"syscall"

	"github.com/gin-gonic/gin"
	"github.com/midgard/apps/wellness/internal/handlers"
	"github.com/midgard/apps/wellness/internal/middleware"
	"github.com/midgard/apps/wellness/internal/routes"
)

func main() {
	// Initialize the stream processor
	handlers.InitStreamProcessor()
	
	// Create a default gin router
	router := gin.Default()

	// Add custom middleware
	router.Use(middleware.Logger())

	// Setup routes
	routes.SetupRoutes(router)

	// Create a channel to listen for OS signals
	sigChan := make(chan os.Signal, 1)
	signal.Notify(sigChan, syscall.SIGINT, syscall.SIGTERM)

	// Start the server in a goroutine
	go func() {
		log.Printf("Starting server on :8080")
		if err := router.Run(":8080"); err != nil {
			log.Fatalf("Failed to start server: %v", err)
		}
	}()

	// Wait for a signal
	sig := <-sigChan
	log.Printf("Received signal: %v", sig)
	
	// Gracefully shut down the server
	log.Println("Shutting down server...")
} 