package sentiment

import (
	"math"
	"strings"
)

// SentimentResult represents the result of sentiment analysis
type SentimentResult struct {
	Text       string  `json:"text"`
	Sentiment  string  `json:"sentiment"` // "positive", "negative", or "neutral"
	Score      float64 `json:"score"`     // Score from -1.0 (very negative) to 1.0 (very positive)
	Confidence float64 `json:"confidence"` // Confidence in the prediction (0.0 to 1.0)
}

// Analyzer is a simple sentiment analysis model
type Analyzer struct {
	// Positive and negative word lists
	positiveWords map[string]bool
	negativeWords map[string]bool
}

// NewAnalyzer creates a new sentiment analyzer
func NewAnalyzer() *Analyzer {
	return &Analyzer{
		positiveWords: map[string]bool{
			"good": true, "great": true, "excellent": true, "amazing": true, "wonderful": true,
			"happy": true, "joy": true, "love": true, "best": true, "perfect": true,
			"beautiful": true, "fantastic": true, "brilliant": true, "outstanding": true,
			"favorite": true, "enjoy": true, "enjoyed": true, "enjoying": true, "enjoys": true,
			"pleased": true, "pleasure": true, "delighted": true, "satisfied": true,
			"recommend": true, "recommended": true, "recommends": true, "recommending": true,
		},
		negativeWords: map[string]bool{
			"bad": true, "terrible": true, "awful": true, "horrible": true, "worst": true,
			"sad": true, "angry": true, "hate": true, "disappointed": true, "poor": true,
			"ugly": true, "dislike": true, "disliked": true, "dislikes": true, "disliking": true,
			"unhappy": true, "unpleasant": true, "unsatisfied": true, "displeased": true,
			"not recommend": true, "not recommended": true, "not recommends": true,
		},
	}
}

// Analyze performs sentiment analysis on the given text
func (a *Analyzer) Analyze(text string) SentimentResult {
	// Convert text to lowercase for case-insensitive matching
	lowerText := strings.ToLower(text)
	
	// Count positive and negative words
	positiveCount := 0
	negativeCount := 0
	
	// Split text into words
	words := strings.Fields(lowerText)
	
	// Count occurrences of positive and negative words
	for _, word := range words {
		if a.positiveWords[word] {
			positiveCount++
		}
		if a.negativeWords[word] {
			negativeCount++
		}
	}
	
	// Calculate sentiment score (-1.0 to 1.0)
	totalWords := len(words)
	if totalWords == 0 {
		return SentimentResult{
			Text:       text,
			Sentiment:  "neutral",
			Score:      0.0,
			Confidence: 0.0,
		}
	}
	
	// Calculate score based on word counts
	score := float64(positiveCount-negativeCount) / float64(totalWords)
	
	// Normalize score to [-1.0, 1.0] range
	score = math.Max(-1.0, math.Min(1.0, score))
	
	// Determine sentiment label
	var sentiment string
	if score > 0.1 {
		sentiment = "positive"
	} else if score < -0.1 {
		sentiment = "negative"
	} else {
		sentiment = "neutral"
	}
	
	// Calculate confidence based on the absolute value of the score
	confidence := math.Abs(score)
	
	return SentimentResult{
		Text:       text,
		Sentiment:  sentiment,
		Score:      score,
		Confidence: confidence,
	}
} 