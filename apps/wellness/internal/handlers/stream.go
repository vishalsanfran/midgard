package handlers

import (
	"context"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/midgard/apps/wellness/internal/stream"
)

// StreamRequest represents the request body for streaming sentiment analysis
type StreamRequest struct {
	Text string `json:"text" binding:"required"`
}

// StreamResponse represents the response from streaming sentiment analysis
type StreamResponse struct {
	ID string `json:"id"`
}

// StreamResult represents a single result from the stream
type StreamResult struct {
	ID        string    `json:"id"`
	Text      string    `json:"text"`
	Sentiment string    `json:"sentiment"`
	Score     float64   `json:"score"`
	Timestamp time.Time `json:"timestamp"`
}

var (
	// Global stream processor
	streamProcessor *stream.StreamProcessor
	// Context for the stream processor
	streamCtx context.Context
	// Cancel function for the stream processor
	streamCancel context.CancelFunc
)

// InitStreamProcessor initializes the stream processor
func InitStreamProcessor() {
	// Create a new context with cancellation
	streamCtx, streamCancel = context.WithCancel(context.Background())
	
	// Create a new stream processor with 5 workers
	streamProcessor = stream.NewStreamProcessor(5)
	
	// Start the processor
	streamProcessor.Start(streamCtx)
	
	// Register a cleanup function to stop the processor when the application exits
	// This would typically be done in a more sophisticated way in a real application
	go func() {
		<-streamCtx.Done()
		streamProcessor.Stop()
	}()
}

// SubmitToStream handles the submission of text to the stream
func SubmitToStream(c *gin.Context) {
	var request StreamRequest
	
	// Bind the request body
	if err := c.ShouldBindJSON(&request); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "Invalid request format. 'text' field is required.",
		})
		return
	}
	
	// Create a new text item
	item := stream.TextItem{
		ID:        fmt.Sprintf("item-%d", time.Now().UnixNano()),
		Text:      request.Text,
		Timestamp: time.Now(),
	}
	
	// Submit the item to the stream processor
	streamProcessor.Process(item)
	
	// Return the item ID
	c.JSON(http.StatusAccepted, StreamResponse{
		ID: item.ID,
	})
}

// StreamResults handles the streaming of sentiment analysis results
func StreamResults(c *gin.Context) {
	// Set headers for SSE
	c.Header("Content-Type", "text/event-stream")
	c.Header("Cache-Control", "no-cache")
	c.Header("Connection", "keep-alive")
	c.Header("Transfer-Encoding", "chunked")
	
	// Create a channel to receive results
	resultsChan := streamProcessor.Results()
	
	// Create a done channel to signal when the client disconnects
	done := make(chan bool)
	
	// Set up a goroutine to detect client disconnection
	go func() {
		<-c.Request.Context().Done()
		done <- true
	}()
	
	// Send an initial message to confirm connection
	c.SSEvent("connected", "Stream connection established")
	
	// Stream results to the client
	c.Stream(func(w io.Writer) bool {
		select {
		case <-done:
			log.Println("Client disconnected")
			return false
		case result, ok := <-resultsChan:
			if !ok {
				log.Println("Results channel closed")
				return false
			}
			
			// Create a stream result
			streamResult := StreamResult{
				ID:        result.Item.ID,
				Text:      result.Item.Text,
				Sentiment: result.Result.Sentiment,
				Score:     result.Result.Score,
				Timestamp: result.Processed,
			}
			
			// Marshal the result to JSON
			data, err := json.Marshal(streamResult)
			if err != nil {
				log.Printf("Error marshaling result: %v", err)
				return true
			}
			
			// Write the result to the client
			c.SSEvent("message", string(data))
			return true
		}
	})
} 