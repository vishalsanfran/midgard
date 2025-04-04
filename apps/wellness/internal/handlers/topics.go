// Package handlers provides HTTP request handlers for the wellness API.
// This file contains handlers related to wellness topic analysis.
package handlers

import (
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/midgard/apps/wellness/internal/topics"
)

// TopicRequest represents the request body for the topic analysis endpoint.
// This is what clients should send when they want to analyze text for wellness topics.
type TopicRequest struct {
	// Text is the content to be analyzed for wellness topics.
	// This should be a string containing the text you want to analyze.
	// Example: "I've been feeling stressed lately and need to exercise more."
	Text string `json:"text" binding:"required"`
}

// TopicResponse represents the response from the topic analysis endpoint.
// This is what clients will receive after submitting a TopicRequest.
type TopicResponse struct {
	// Result contains the analysis results, including the identified topics
	// and a confidence score for the analysis.
	Result topics.WellnessTopicResult `json:"result"`
}

// topicAnalyzer is a global instance of the topics.Analyzer that's used
// to analyze text for wellness topics. It's initialized once when the
// package is loaded and reused for all requests.
var topicAnalyzer = topics.NewAnalyzer()

// AnalyzeTopics handles HTTP POST requests to the /api/v1/topics endpoint.
// This handler:
// 1. Receives a JSON request with a "text" field
// 2. Validates that the text field is present
// 3. Uses the topic analyzer to identify wellness topics in the text
// 4. Returns a JSON response with the analysis results
//
// Example request:
//   POST /api/v1/topics
//   {
//     "text": "I've been feeling stressed lately and need to exercise more."
//   }
//
// Example response:
//   {
//     "result": {
//       "text": "I've been feeling stressed lately and need to exercise more.",
//       "topics": ["mental_health", "physical_health"],
//       "confidence": 0.25
//     }
//   }
//
// Possible response codes:
// - 200 OK: The analysis was successful
// - 400 Bad Request: The request was missing the required "text" field
func AnalyzeTopics(c *gin.Context) {
	var request TopicRequest
	
	// Bind the request body
	if err := c.ShouldBindJSON(&request); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "Invalid request format. 'text' field is required.",
		})
		return
	}
	
	// Perform topic analysis
	result := topicAnalyzer.Analyze(request.Text)
	
	// Return the result
	c.JSON(http.StatusOK, TopicResponse{
		Result: result,
	})
} 