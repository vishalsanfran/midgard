package handlers

import (
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/midgard/apps/wellness/internal/sentiment"
)

// SentimentRequest represents the request body for sentiment analysis
type SentimentRequest struct {
	Text string `json:"text" binding:"required"`
}

// SentimentResponse represents the response from sentiment analysis
type SentimentResponse struct {
	Result sentiment.SentimentResult `json:"result"`
}

var sentimentAnalyzer = sentiment.NewAnalyzer()

// AnalyzeSentiment handles the sentiment analysis endpoint
func AnalyzeSentiment(c *gin.Context) {
	var request SentimentRequest
	
	// Bind the request body
	if err := c.ShouldBindJSON(&request); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "Invalid request format. 'text' field is required.",
		})
		return
	}
	
	// Perform sentiment analysis
	result := sentimentAnalyzer.Analyze(request.Text)
	
	// Return the result
	c.JSON(http.StatusOK, SentimentResponse{
		Result: result,
	})
} 