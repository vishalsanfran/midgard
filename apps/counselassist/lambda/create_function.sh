#!/bin/bash

# Check if AWS account ID is provided
if [ -z "$1" ]; then
    echo "Usage: ./create_function.sh <aws-account-id>"
    exit 1
fi

ACCOUNT_ID=$1

# Create Lambda function with limits
aws lambda create-function \
    --function-name counselassist \
    --runtime python3.9 \
    --handler lambda_handler.handler \
    --role "arn:aws:iam::${ACCOUNT_ID}:role/lambda-role" \
    --timeout 10 \
    --memory-size 256 \
    --zip-file fileb://deployment.zip

# # After function is created, set concurrency limit
# aws lambda put-function-concurrency \
#     --function-name counselassist \
#     --reserved-concurrent-executions 5

# # Create usage plan
# aws apigateway create-usage-plan \
#     --name "counselassist-usage-plan" \
#     --throttle {\"burstLimit\":10,\"rateLimit\":5} \
#     --quota {\"limit\":1000,\"period\":\"DAY\"}

# # Create CloudWatch alarm
# aws cloudwatch put-metric-alarm \
#     --alarm-name counselassist-daily-requests \
#     --alarm-description "Alert when nearing daily request limit" \
#     --metric-name Invocations \
#     --namespace AWS/Lambda \
#     --statistic Sum \
#     --period 86400 \
#     --threshold 900 \
#     --comparison-operator GreaterThanThreshold \
#     --evaluation-periods 1 \
#     --dimensions Name=FunctionName,Value=counselassist

echo "Function created with usage limits!"