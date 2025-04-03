package handlers

import (
	"bytes"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/gin-gonic/gin"
)

func TestSubmitToStream(t *testing.T) {
	// Set Gin to test mode
	gin.SetMode(gin.TestMode)
	
	// Initialize the stream processor for testing
	InitStreamProcessor()
	defer func() {
		// Clean up the stream processor after the test
		if streamCancel != nil {
			streamCancel()
		}
	}()

	tests := []struct {
		name           string
		requestBody    interface{}
		expectedStatus int
	}{
		{
			name: "Valid request",
			requestBody: StreamRequest{
				Text: "I really enjoyed this product, it's amazing!",
			},
			expectedStatus: http.StatusAccepted,
		},
		{
			name: "Empty request body",
			requestBody: map[string]interface{}{
				"text": "",
			},
			expectedStatus: http.StatusBadRequest,
		},
		{
			name: "Missing text field",
			requestBody: map[string]interface{}{
				"other_field": "value",
			},
			expectedStatus: http.StatusBadRequest,
		},
		{
			name:           "Invalid JSON",
			requestBody:    "invalid json",
			expectedStatus: http.StatusBadRequest,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			// Create a new Gin router
			router := gin.New()
			router.POST("/stream", SubmitToStream)

			// Create a request
			var reqBody []byte
			var err error
			if str, ok := tt.requestBody.(string); ok {
				reqBody = []byte(str)
			} else {
				reqBody, err = json.Marshal(tt.requestBody)
				if err != nil {
					t.Fatalf("Failed to marshal request body: %v", err)
				}
			}

			// Create a test request
			req, err := http.NewRequest("POST", "/stream", bytes.NewBuffer(reqBody))
			if err != nil {
				t.Fatalf("Failed to create request: %v", err)
			}
			req.Header.Set("Content-Type", "application/json")

			// Create a response recorder
			w := httptest.NewRecorder()

			// Serve the request
			router.ServeHTTP(w, req)

			// Check the status code
			if w.Code != tt.expectedStatus {
				t.Errorf("SubmitToStream() status = %v, want %v", w.Code, tt.expectedStatus)
			}

			// If the status is OK, check the response body
			if tt.expectedStatus == http.StatusAccepted {
				var response StreamResponse
				if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
					t.Fatalf("Failed to unmarshal response: %v", err)
				}

				// Check that the response contains an ID
				if response.ID == "" {
					t.Error("SubmitToStream() response ID is empty")
				}
			}
		})
	}
}

func TestStreamResultsHeaders(t *testing.T) {
	// Set Gin to test mode
	gin.SetMode(gin.TestMode)
	
	// Initialize the stream processor for testing
	InitStreamProcessor()
	defer func() {
		// Clean up the stream processor after the test
		if streamCancel != nil {
			streamCancel()
		}
	}()

	// Create a new Gin router
	router := gin.New()
	router.GET("/stream/results", func(c *gin.Context) {
		// Set headers for SSE
		c.Header("Content-Type", "text/event-stream")
		c.Header("Cache-Control", "no-cache")
		c.Header("Connection", "keep-alive")
		c.Header("Transfer-Encoding", "chunked")
		
		// Return immediately for testing
		c.Status(http.StatusOK)
	})

	// Create a test request
	req, err := http.NewRequest("GET", "/stream/results", nil)
	if err != nil {
		t.Fatalf("Failed to create request: %v", err)
	}

	// Create a response recorder
	w := httptest.NewRecorder()

	// Serve the request
	router.ServeHTTP(w, req)

	// Check the status code
	if w.Code != http.StatusOK {
		t.Errorf("StreamResults() status = %v, want %v", w.Code, http.StatusOK)
	}

	// Check the content type
	contentType := w.Header().Get("Content-Type")
	if contentType != "text/event-stream" {
		t.Errorf("StreamResults() content type = %v, want %v", contentType, "text/event-stream")
	}

	// Check that the response contains SSE headers
	if w.Header().Get("Cache-Control") != "no-cache" {
		t.Errorf("StreamResults() cache control = %v, want %v", w.Header().Get("Cache-Control"), "no-cache")
	}
	if w.Header().Get("Connection") != "keep-alive" {
		t.Errorf("StreamResults() connection = %v, want %v", w.Header().Get("Connection"), "keep-alive")
	}
} 