export interface PredictionRequest {
  text: string;
  model_version?: string | null;
}

export interface PredictionResponse {
  prediction: number;
  confidence: number;
  model_version: string;
  timestamp: string;
  interpretation: string;  // Added this field to match the API response
}