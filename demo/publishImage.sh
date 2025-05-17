#!/bin/bash

# Variables
IMAGE_NAME="rubencamach0/bookhive:1.0.0"

# Login
echo "Logging into Docker Hub..."
docker login

# Push
echo "Pushing Docker image: $IMAGE_NAME"
docker push $IMAGE_NAME
