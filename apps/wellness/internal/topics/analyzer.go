// Package topics provides functionality for analyzing text and identifying wellness-related topics.
// This package helps identify what aspects of wellness a text is discussing, such as mental health,
// physical health, emotional wellbeing, social connection, and spiritual wellness.
package topics

import (
	"strings"
	"github.com/james-bowman/nlp"
)

// WellnessTopicResult represents the result of topic analysis.
// It contains the original text, the identified wellness topics, and a confidence score.
type WellnessTopicResult struct {
	// Text is the original input text that was analyzed
	Text string `json:"text"`
	
	// Topics is a list of wellness topics identified in the text
	// Possible values include: "mental_health", "physical_health", "emotional_wellbeing",
	// "social_connection", and "spiritual_wellness"
	Topics []string `json:"topics"`
	
	// Confidence is a score from 0.0 to 1.0 indicating how confident the analyzer is
	// in its topic identification. Higher values indicate more confidence.
	Confidence float64 `json:"confidence"`
}

// Analyzer is a tool that analyzes text to identify wellness-related topics.
// It uses natural language processing techniques to recognize topics based on
// predefined keywords and phrases associated with different wellness categories.
type Analyzer struct {
	// vectorizer converts text into numerical vectors for analysis
	vectorizer *nlp.CountVectoriser
	
	// transformer applies TF-IDF (Term Frequency-Inverse Document Frequency)
	// weighting to improve the quality of the analysis
	transformer *nlp.TfidfTransformer
	
	// topics maps each wellness category to a list of related terms
	// For example, "mental_health" is associated with terms like "anxiety", "stress", etc.
	topics map[string][]string
}

// NewAnalyzer creates a new wellness topic analyzer with predefined wellness categories
// and their associated terms. This function initializes the analyzer with common
// wellness-related vocabulary to help identify topics in text.
func NewAnalyzer() *Analyzer {
	// Create a vectorizer with common stop words
	vectorizer := nlp.NewCountVectoriser()
	
	// Create a TF-IDF transformer
	transformer := nlp.NewTfidfTransformer()
	
	// Define wellness topics and their related terms
	// Each topic has a list of words and phrases that are commonly associated with it
	topics := map[string][]string{
		"mental_health": {"anxiety", "depression", "stress", "meditation", "mindfulness", "therapy", "counseling", "mental", "psychology"},
		"physical_health": {"exercise", "workout", "fitness", "nutrition", "diet", "sleep", "rest", "recovery", "strength", "cardio"},
		"emotional_wellbeing": {"emotions", "feelings", "happiness", "joy", "sadness", "anger", "fear", "gratitude", "positivity"},
		"social_connection": {"friends", "family", "community", "support", "relationships", "connection", "social", "interaction"},
		"spiritual_wellness": {"spirituality", "purpose", "meaning", "values", "beliefs", "meditation", "mindfulness", "reflection"},
	}
	
	return &Analyzer{
		vectorizer: vectorizer,
		transformer: transformer,
		topics: topics,
	}
}

// Analyze performs topic analysis on the given text and returns a WellnessTopicResult.
// This function:
// 1. Converts the text to lowercase for case-insensitive matching
// 2. Splits the text into individual words
// 3. Counts how many times each wellness topic's terms appear in the text
// 4. Identifies the most relevant topics based on term frequency
// 5. Calculates a confidence score based on how many topic terms were found
//
// Example usage:
//   analyzer := topics.NewAnalyzer()
//   result := analyzer.Analyze("I've been feeling stressed and need to exercise more.")
//   // result.Topics might contain ["mental_health", "physical_health"]
func (a *Analyzer) Analyze(text string) WellnessTopicResult {
	// Convert text to lowercase for case-insensitive matching
	lowerText := strings.ToLower(text)
	
	// Split text into words
	words := strings.Fields(lowerText)
	
	// Count occurrences of topic-related terms
	topicScores := make(map[string]int)
	
	for _, word := range words {
		for topic, terms := range a.topics {
			for _, term := range terms {
				if strings.Contains(word, term) {
					topicScores[topic]++
				}
			}
		}
	}
	
	// Find the top topics
	var topTopics []string
	
	// Find the maximum score
	maxScore := 0
	for _, score := range topicScores {
		if score > maxScore {
			maxScore = score
		}
	}
	
	// Add all topics with a score above a threshold (half of the max score)
	// This ensures we include multiple topics when they're both relevant
	threshold := maxScore / 2
	if threshold < 1 && maxScore > 0 {
		threshold = 1 // Ensure at least one match is required
	}
	
	for topic, score := range topicScores {
		if score >= threshold {
			topTopics = append(topTopics, topic)
		}
	}
	
	// Calculate confidence based on the number of matches
	confidence := 0.0
	if len(words) > 0 {
		totalMatches := 0
		for _, score := range topicScores {
			totalMatches += score
		}
		confidence = float64(totalMatches) / float64(len(words))
	}
	
	return WellnessTopicResult{
		Text:       text,
		Topics:     topTopics,
		Confidence: confidence,
	}
} 