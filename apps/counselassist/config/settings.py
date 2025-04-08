from pydantic_settings import BaseSettings
from pathlib import Path

class Settings(BaseSettings):
    APP_NAME: str = "CounselAssist"
    MODEL_DIR: Path = Path("models")
    DEFAULT_MODEL_VERSION: str = "v1"
    PERFORMANCE_THRESHOLD: float = 0.75
    
    class Config:
        env_file = ".env"

settings = Settings()