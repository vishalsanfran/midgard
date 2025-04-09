from mangum import Mangum
import sys
import os

# Add the parent directory to sys.path
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from app import app

# Create handler for Lambda
handler = Mangum(app)