// Package handlers provides HTTP request handlers for the wellness API.
// This file contains tests for the wellness topic analysis handler.
package handlers

import (
	"bytes"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/gin-gonic/gin"
	"github.com/stretchr/testify/assert"
)

// TestAnalyzeTopics tests the AnalyzeTopics handler function.
// This test:
// 1. Sets up a test Gin router with the topics endpoint
// 2. Creates test cases with different input texts
// 3. Sends HTTP requests to the endpoint
// 4. Verifies that the responses contain the expected topics
// 5. Checks that error cases are handled correctly
func TestAnalyzeTopics(t *testing.T) {
	// Set Gin to test mode
	gin.SetMode(gin.TestMode)

	// Create a new Gin router with only the topics endpoint
	router := gin.New()
	router.POST("/topics", AnalyzeTopics)

	// Define test cases with different input texts and expected results
	tests := []struct {
		name           string
		requestBody    TopicRequest
		expectedStatus int
		expectedTopics []string
	}{
		{
			name: "Valid request with mental health topic",
			requestBody: TopicRequest{
				Text: "I've been feeling anxious and stressed lately. I think I need to try meditation.",
			},
			expectedStatus: http.StatusOK,
			expectedTopics: []string{"mental_health"},
		},
		{
			name: "Valid request with multiple topics",
			requestBody: TopicRequest{
				Text: "I've been feeling stressed and need to exercise more. Meditation helps with mindfulness.",
			},
			expectedStatus: http.StatusOK,
			expectedTopics: []string{"mental_health", "physical_health"},
		},
		{
			name: "Empty text",
			requestBody: TopicRequest{
				Text: "",
			},
			expectedStatus: http.StatusBadRequest,
			expectedTopics: nil,
		},
	}

	// Run each test case
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			// Create a request
			jsonBody, err := json.Marshal(tt.requestBody)
			assert.NoError(t, err)

			req, err := http.NewRequest("POST", "/topics", bytes.NewBuffer(jsonBody))
			assert.NoError(t, err)
			req.Header.Set("Content-Type", "application/json")

			// Create a response recorder
			w := httptest.NewRecorder()

			// Perform the request
			router.ServeHTTP(w, req)

			// Check the status code
			assert.Equal(t, tt.expectedStatus, w.Code)

			// If the status is OK, check the response body
			if tt.expectedStatus == http.StatusOK {
				var response TopicResponse
				err = json.Unmarshal(w.Body.Bytes(), &response)
				assert.NoError(t, err)

				// Check if the result text matches the input
				assert.Equal(t, tt.requestBody.Text, response.Result.Text)

				// Check if the expected topics are in the result
				if len(tt.expectedTopics) == 0 {
					assert.Empty(t, response.Result.Topics)
				} else {
					for _, expectedTopic := range tt.expectedTopics {
						found := false
						for _, resultTopic := range response.Result.Topics {
							if resultTopic == expectedTopic {
								found = true
								break
							}
						}
						assert.True(t, found, "Expected topic %v not found in %v", expectedTopic, response.Result.Topics)
					}
				}

				// Check if the confidence is a valid value
				assert.GreaterOrEqual(t, response.Result.Confidence, 0.0)
				assert.LessOrEqual(t, response.Result.Confidence, 1.0)
			}
		})
	}
} 