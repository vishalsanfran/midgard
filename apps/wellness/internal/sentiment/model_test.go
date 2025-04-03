package sentiment

import (
	"testing"
)

func TestAnalyze(t *testing.T) {
	analyzer := NewAnalyzer()

	tests := []struct {
		name           string
		text           string
		expectedSentiment string
		expectedScore    float64
		expectedConfidence float64
	}{
		{
			name:           "Positive sentiment",
			text:           "I really enjoyed this product, it's amazing!",
			expectedSentiment: "positive",
			expectedScore:    0.14285714285714285, // 2 positive words / 14 total words
			expectedConfidence: 0.14285714285714285,
		},
		{
			name:           "Negative sentiment",
			text:           "This product is terrible, I hate it!",
			expectedSentiment: "negative",
			expectedScore:    -0.14285714285714285, // 2 negative words / 14 total words
			expectedConfidence: 0.14285714285714285,
		},
		{
			name:           "Neutral sentiment",
			text:           "This is a regular product.",
			expectedSentiment: "neutral",
			expectedScore:    0.0,
			expectedConfidence: 0.0,
		},
		{
			name:           "Empty text",
			text:           "",
			expectedSentiment: "neutral",
			expectedScore:    0.0,
			expectedConfidence: 0.0,
		},
		{
			name:           "Mixed sentiment",
			text:           "I love the design but hate the price.",
			expectedSentiment: "neutral",
			expectedScore:    0.0,
			expectedConfidence: 0.0,
		},
		{
			name:           "Strong positive",
			text:           "This is the best product ever! I love it so much!",
			expectedSentiment: "positive",
			expectedScore:    0.18181818181818182, // 2 positive words / 11 total words
			expectedConfidence: 0.18181818181818182,
		},
		{
			name:           "Strong negative",
			text:           "This is the worst product ever! I hate it so much!",
			expectedSentiment: "negative",
			expectedScore:    -0.18181818181818182, // 2 negative words / 11 total words
			expectedConfidence: 0.18181818181818182,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			result := analyzer.Analyze(tt.text)

			// Check text is preserved
			if result.Text != tt.text {
				t.Errorf("Analyze() text = %v, want %v", result.Text, tt.text)
			}

			// Check sentiment
			if result.Sentiment != tt.expectedSentiment {
				t.Errorf("Analyze() sentiment = %v, want %v", result.Sentiment, tt.expectedSentiment)
			}

			// Check score (with some tolerance for floating point)
			if result.Score < tt.expectedScore-0.0001 || result.Score > tt.expectedScore+0.0001 {
				t.Errorf("Analyze() score = %v, want %v", result.Score, tt.expectedScore)
			}

			// Check confidence (with some tolerance for floating point)
			if result.Confidence < tt.expectedConfidence-0.0001 || result.Confidence > tt.expectedConfidence+0.0001 {
				t.Errorf("Analyze() confidence = %v, want %v", result.Confidence, tt.expectedConfidence)
			}
		})
	}
}

func TestNewAnalyzer(t *testing.T) {
	analyzer := NewAnalyzer()

	// Check that the analyzer was created
	if analyzer == nil {
		t.Fatal("NewAnalyzer() returned nil")
	}

	// Check that the word lists were initialized
	if analyzer.positiveWords == nil {
		t.Fatal("positiveWords map is nil")
	}
	if analyzer.negativeWords == nil {
		t.Fatal("negativeWords map is nil")
	}

	// Check that some expected words are in the lists
	expectedPositiveWords := []string{"good", "great", "excellent", "amazing", "wonderful"}
	for _, word := range expectedPositiveWords {
		if !analyzer.positiveWords[word] {
			t.Errorf("Expected positive word '%s' not found in positiveWords map", word)
		}
	}

	expectedNegativeWords := []string{"bad", "terrible", "awful", "horrible", "worst"}
	for _, word := range expectedNegativeWords {
		if !analyzer.negativeWords[word] {
			t.Errorf("Expected negative word '%s' not found in negativeWords map", word)
		}
	}
} 