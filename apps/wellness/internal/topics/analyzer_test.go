// Package topics provides functionality for analyzing text and identifying wellness-related topics.
// This file contains tests for the wellness topic analyzer.
package topics

import (
	"testing"
)

// TestAnalyzer_Analyze tests the Analyze method of the Analyzer struct.
// This test:
// 1. Creates a new analyzer
// 2. Tests it with various input texts that should trigger different wellness topics
// 3. Verifies that the correct topics are identified
// 4. Checks that the confidence scores are reasonable
// 5. Tests edge cases like text with no wellness topics
func TestAnalyzer_Analyze(t *testing.T) {
	// Create a new analyzer for testing
	analyzer := NewAnalyzer()

	// Define test cases with different input texts and expected results
	tests := []struct {
		name           string
		text           string
		expectedTopics []string
		minConfidence  float64
	}{
		{
			name:           "Mental health topic",
			text:           "I've been feeling anxious and stressed lately. I think I need to try meditation.",
			expectedTopics: []string{"mental_health"},
			minConfidence:  0.1,
		},
		{
			name:           "Physical health topic",
			text:           "I need to start exercising more and improve my diet. Sleep has been difficult.",
			expectedTopics: []string{"physical_health"},
			minConfidence:  0.1,
		},
		{
			name:           "Emotional wellbeing topic",
			text:           "I'm feeling happy and grateful for my family. Joy fills my heart.",
			expectedTopics: []string{"emotional_wellbeing"},
			minConfidence:  0.1,
		},
		{
			name:           "Social connection topic",
			text:           "I spent time with friends and family. Community support is important.",
			expectedTopics: []string{"social_connection"},
			minConfidence:  0.1,
		},
		{
			name:           "Spiritual wellness topic",
			text:           "I've been reflecting on my purpose and values. Spirituality gives meaning to my life.",
			expectedTopics: []string{"spiritual_wellness"},
			minConfidence:  0.1,
		},
		{
			name:           "Multiple topics",
			text:           "I've been feeling stressed and need to exercise more. Meditation helps with mindfulness.",
			expectedTopics: []string{"mental_health", "physical_health"},
			minConfidence:  0.1,
		},
		{
			name:           "No topics",
			text:           "The weather is nice today and I went for a walk.",
			expectedTopics: []string{},
			minConfidence:  0.0,
		},
	}

	// Run each test case
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			// Analyze the test text
			result := analyzer.Analyze(tt.text)

			// Check if the result text matches the input
			if result.Text != tt.text {
				t.Errorf("Analyze() text = %v, want %v", result.Text, tt.text)
			}

			// Check if the expected topics are in the result
			if len(tt.expectedTopics) == 0 && len(result.Topics) > 0 {
				t.Errorf("Analyze() topics = %v, want empty", result.Topics)
			} else if len(tt.expectedTopics) > 0 {
				// Check if all expected topics are in the result
				for _, expectedTopic := range tt.expectedTopics {
					found := false
					for _, resultTopic := range result.Topics {
						if resultTopic == expectedTopic {
							found = true
							break
						}
					}
					if !found {
						t.Errorf("Analyze() missing expected topic %v in %v", expectedTopic, result.Topics)
					}
				}
			}

			// Check if the confidence is at least the minimum expected
			if result.Confidence < tt.minConfidence {
				t.Errorf("Analyze() confidence = %v, want >= %v", result.Confidence, tt.minConfidence)
			}
		})
	}
} 