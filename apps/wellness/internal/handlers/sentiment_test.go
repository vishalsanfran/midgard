package handlers

import (
	"bytes"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/gin-gonic/gin"
	"github.com/midgard/apps/wellness/internal/sentiment"
)

func TestAnalyzeSentiment(t *testing.T) {
	// Set Gin to test mode
	gin.SetMode(gin.TestMode)

	tests := []struct {
		name           string
		requestBody    string
		expectedStatus int
		expectedResult sentiment.SentimentResult
	}{
		{
			name:           "Valid positive sentiment request",
			requestBody:    `{"text": "I really enjoyed this product, it's amazing!"}`,
			expectedStatus: http.StatusOK,
			expectedResult: sentiment.SentimentResult{
				Text:       "I really enjoyed this product, it's amazing!",
				Sentiment:  "positive",
				Score:      0.14285714285714285, // 2 positive words / 14 total words
				Confidence: 0.14285714285714285,
			},
		},
		{
			name:           "Valid negative sentiment request",
			requestBody:    `{"text": "This product is terrible, I hate it!"}`,
			expectedStatus: http.StatusOK,
			expectedResult: sentiment.SentimentResult{
				Text:       "This product is terrible, I hate it!",
				Sentiment:  "negative",
				Score:      -0.14285714285714285, // 2 negative words / 14 total words
				Confidence: 0.14285714285714285,
			},
		},
		{
			name:           "Valid neutral sentiment request",
			requestBody:    `{"text": "This is a regular product."}`,
			expectedStatus: http.StatusOK,
			expectedResult: sentiment.SentimentResult{
				Text:       "This is a regular product.",
				Sentiment:  "neutral",
				Score:      0.0,
				Confidence: 0.0,
			},
		},
		{
			name:           "Empty request body",
			requestBody:    `{}`,
			expectedStatus: http.StatusBadRequest,
		},
		{
			name:           "Missing text field",
			requestBody:    `{"other_field": "value"}`,
			expectedStatus: http.StatusBadRequest,
		},
		{
			name:           "Invalid JSON",
			requestBody:    `{"text": "This is invalid JSON`,
			expectedStatus: http.StatusBadRequest,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			// Create a new Gin router
			router := gin.New()
			router.POST("/sentiment", AnalyzeSentiment)

			// Create a request
			req, err := http.NewRequest("POST", "/sentiment", bytes.NewBufferString(tt.requestBody))
			if err != nil {
				t.Fatal(err)
			}
			req.Header.Set("Content-Type", "application/json")

			// Create a response recorder
			w := httptest.NewRecorder()

			// Serve the request
			router.ServeHTTP(w, req)

			// Check the status code
			if w.Code != tt.expectedStatus {
				t.Errorf("AnalyzeSentiment() status = %v, want %v", w.Code, tt.expectedStatus)
			}

			// If we expect a successful response, check the result
			if tt.expectedStatus == http.StatusOK {
				var response SentimentResponse
				err = json.Unmarshal(w.Body.Bytes(), &response)
				if err != nil {
					t.Fatalf("Failed to unmarshal response: %v", err)
				}

				// Check the result
				result := response.Result
				if result.Text != tt.expectedResult.Text {
					t.Errorf("AnalyzeSentiment() text = %v, want %v", result.Text, tt.expectedResult.Text)
				}
				if result.Sentiment != tt.expectedResult.Sentiment {
					t.Errorf("AnalyzeSentiment() sentiment = %v, want %v", result.Sentiment, tt.expectedResult.Sentiment)
				}
				if result.Score < tt.expectedResult.Score-0.0001 || result.Score > tt.expectedResult.Score+0.0001 {
					t.Errorf("AnalyzeSentiment() score = %v, want %v", result.Score, tt.expectedResult.Score)
				}
				if result.Confidence < tt.expectedResult.Confidence-0.0001 || result.Confidence > tt.expectedResult.Confidence+0.0001 {
					t.Errorf("AnalyzeSentiment() confidence = %v, want %v", result.Confidence, tt.expectedResult.Confidence)
				}
			}
		})
	}
} 