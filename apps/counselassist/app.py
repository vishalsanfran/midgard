"""
CounselAssist API Service
-------------------------
FastAPI service that provides mental health counseling response predictions
using machine learning models. Supports multiple model versions, metrics tracking,
and real-time predictions.
"""

from fastapi import FastAPI, BackgroundTasks, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import joblib
import numpy as np
from datetime import datetime
from pathlib import Path
import logging
from typing import Dict, Optional

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class PredictionRequest(BaseModel):
    text: str
    model_version: Optional[str] = None

class PredictionResponse(BaseModel):
    prediction: float
    confidence: float
    model_version: str
    timestamp: str
    interpretation: str

class ModelService:
    def __init__(self):
        self.models: Dict[str, any] = {}
        self.current_version = None
        self.metrics = {
            "predictions": 0,
            "errors": 0,
            "avg_confidence": 0.0,
            "last_updated": None
        }
        self.load_models()
        if not self.models:
            logger.error("No models found in models directory")
            raise RuntimeError("No models available")

    def load_models(self):
        try:
            models_dir = Path("models")
            if not models_dir.exists():
                logger.error(f"Models directory not found: {models_dir.absolute()}")
                return

            for model_path in models_dir.glob("counselor_response_classifier_*.joblib"):
                version = model_path.stem.split("_")[-1]  # Get the git hash
                logger.info(f"Loading model from: {model_path}")
                self.models[version] = joblib.load(model_path)
                if not self.current_version:
                    self.current_version = version
                logger.info(f"Loaded model version: {version}")
        except Exception as e:
            logger.error(f"Error loading models: {str(e)}", exc_info=True)
            raise

    def get_model(self, version: Optional[str] = None):
        try:
            if version and version not in self.models:
                raise KeyError(f"Model version {version} not found. Available versions: {list(self.models.keys())}")
            if not self.current_version:
                raise RuntimeError("No models loaded")
            return self.models[version or self.current_version]
        except Exception as e:
            logger.error(f"Error getting model: {str(e)}")
            raise

    def update_metrics(self, confidence: float):
        self.metrics["predictions"] += 1
        n = self.metrics["predictions"]
        self.metrics["avg_confidence"] = (
            (self.metrics["avg_confidence"] * (n-1) + confidence) / n
        )

# 5. Create FastAPI app and configure middleware
app = FastAPI(
    title="CounselAssist API",
    description="Mental health counseling response prediction API",
    version="1.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=False,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 6. Initialize service instance
model_service = ModelService()

# 7. Define routes
@app.post("/predict", response_model=PredictionResponse, tags=["predictions"])
async def predict(request: PredictionRequest, background_tasks: BackgroundTasks):
    try:
        logger.info(f"Processing prediction request for version: {request.model_version}")
        model = model_service.get_model(request.model_version)
        
        # Get probabilities for all classes
        probabilities = model.predict_proba([request.text])[0]
        
        # Get the predicted class probability (highest probability)
        prediction = probabilities[1]  # Probability for positive class
        
        # Get confidence (margin between top two probabilities)
        sorted_probs = sorted(probabilities, reverse=True)
        confidence = sorted_probs[0] - sorted_probs[1]  # Difference between top two probabilities
        
        background_tasks.add_task(model_service.update_metrics, confidence)
        
        # Add interpretation of the prediction
        interpretation = (
            f"There is a {prediction*100:.1f}% likelihood that this situation "
            f"would receive direct advice or guidance, as opposed to empathetic "
            f"listening or exploratory questions. Confidence: {confidence*100:.1f}%"
        )
        
        response = PredictionResponse(
            prediction=float(prediction),
            confidence=float(confidence),
            model_version=request.model_version or model_service.current_version,
            timestamp=datetime.now().isoformat(),
            interpretation=interpretation  # Add interpretation to response
        )
        logger.info(f"Prediction successful: {response}")
        return response
    except Exception as e:
        logger.error(f"Prediction error: {str(e)}", exc_info=True)
        model_service.metrics["errors"] += 1
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/metrics", tags=["monitoring"])
async def get_metrics():
    return model_service.metrics

@app.post("/models/update", tags=["model-management"])
async def update_model(version: str):
    try:
        new_model_path = f"models/counselor_response_classifier_{version}.joblib"
        model_service.models[version] = joblib.load(new_model_path)
        model_service.current_version = version
        model_service.metrics["last_updated"] = datetime.now().isoformat()
        return {"message": f"Successfully updated to model version {version}"}
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))

@app.get("/models/versions", tags=["model-management"])
async def list_models():
    return {
        "current_version": model_service.current_version,
        "available_versions": list(model_service.models.keys())
    }

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)