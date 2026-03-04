#!/bin/bash

# Exit immediately if a command exits with a non-zero status.
set -e

MODULE=$1

if [ -z "$MODULE" ]; then
  echo "Usage: $0 <module-name>"
  echo "Available modules:"
  ls charts/apps/values-*.yaml | sed 's/charts\/apps\/values-//;s/.yaml//'
  exit 1
fi

VALUES_FILE="charts/apps/values-$MODULE.yaml"

if [ ! -f "$VALUES_FILE" ]; then
  echo "Error: Values file $VALUES_FILE not found."
  echo "Available modules:"
  ls charts/apps/values-*.yaml | sed 's/charts\/apps\/values-//;s/.yaml//'
  exit 1
fi

NAMESPACE="apps"
RELEASE_NAME="benchmark-app"
CHART_PATH="charts/apps"

echo "Deploying $MODULE to namespace $NAMESPACE..."

helm upgrade --install "$RELEASE_NAME" "$CHART_PATH" \
  --namespace "$NAMESPACE" \
  --create-namespace \
  -f "$CHART_PATH/values.yaml" \
  -f "$VALUES_FILE" \
  --wait

echo "Successfully deployed $MODULE."
