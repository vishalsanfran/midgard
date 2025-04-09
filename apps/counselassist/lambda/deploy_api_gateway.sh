#!/bin/bash

set -e
# Set required environment variables
export AWS_DEFAULT_REGION=us-east-1
ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)

# Create API Gateway
API_ID=$(aws apigateway create-rest-api \
    --name "counselassist-api" \
    --query 'id' --output text)

echo "Created API Gateway with ID: $API_ID"

# Get root resource ID
ROOT_ID=$(aws apigateway get-resources \
    --rest-api-id $API_ID \
    --query 'items[0].id' --output text)

# Create resource
RESOURCE_ID=$(aws apigateway create-resource \
    --rest-api-id $API_ID \
    --parent-id $ROOT_ID \
    --path-part "predict" \
    --query 'id' --output text)

# Create POST method
aws apigateway put-method \
    --rest-api-id $API_ID \
    --resource-id $RESOURCE_ID \
    --http-method POST \
    --authorization-type NONE \
    --no-api-key-required

# Set Lambda integration
aws apigateway put-integration \
    --rest-api-id $API_ID \
    --resource-id $RESOURCE_ID \
    --http-method POST \
    --type AWS_PROXY \
    --integration-http-method POST \
    --uri arn:aws:apigateway:${AWS_DEFAULT_REGION}:lambda:path/2015-03-31/functions/arn:aws:lambda:${AWS_DEFAULT_REGION}:${ACCOUNT_ID}:function:counselassist/invocations

# Add Lambda permission for API Gateway
aws lambda add-permission \
    --function-name counselassist \
    --statement-id apigateway-test \
    --action lambda:InvokeFunction \
    --principal apigateway.amazonaws.com \
    --source-arn "arn:aws:execute-api:${AWS_DEFAULT_REGION}:${ACCOUNT_ID}:${API_ID}/*/*/*"

# Enable CORS
aws apigateway put-method-response \
    --rest-api-id $API_ID \
    --resource-id $RESOURCE_ID \
    --http-method POST \
    --status-code 200 \
    --response-parameters "method.response.header.Access-Control-Allow-Origin=true"

# Add integration response
aws apigateway put-integration-response \
    --rest-api-id $API_ID \
    --resource-id $RESOURCE_ID \
    --http-method POST \
    --status-code 200 \
    --response-parameters '{"method.response.header.Access-Control-Allow-Origin": "'"'*'"'"}' \
    --selection-pattern ""

# Add OPTIONS method for CORS
aws apigateway put-method \
    --rest-api-id $API_ID \
    --resource-id $RESOURCE_ID \
    --http-method OPTIONS \
    --authorization-type NONE

# Add OPTIONS method response
aws apigateway put-method-response \
    --rest-api-id $API_ID \
    --resource-id $RESOURCE_ID \
    --http-method OPTIONS \
    --status-code 200 \
    --response-parameters "method.response.header.Access-Control-Allow-Origin=true,method.response.header.Access-Control-Allow-Methods=true,method.response.header.Access-Control-Allow-Headers=true"

# Add OPTIONS method integration
aws apigateway put-integration \
    --rest-api-id $API_ID \
    --resource-id $RESOURCE_ID \
    --http-method OPTIONS \
    --type MOCK \
    --request-templates '{"application/json": "{\"statusCode\": 200}"}'

# Add OPTIONS integration response
aws apigateway put-integration-response \
    --rest-api-id $API_ID \
    --resource-id $RESOURCE_ID \
    --http-method OPTIONS \
    --status-code 200 \
    --response-parameters '{
        "method.response.header.Access-Control-Allow-Origin": "'"'*'"'",
        "method.response.header.Access-Control-Allow-Methods": "'"'POST,OPTIONS'"'",
        "method.response.header.Access-Control-Allow-Headers": "'"'Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token'"'"
    }'

# Then continue with deployment
aws apigateway create-deployment \
    --rest-api-id $API_ID \
    --stage-name $API_STAGE_NAME

aws apigateway get-stage \
    --rest-api-id $API_ID \
    --stage-name $API_STAGE_NAME \
    --query 'invokeUrl' \
    --output text