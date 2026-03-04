#!/usr/bin/env bash

# Always run from the script directory to find body.json
SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
cd "$SCRIPT_DIR"

bombardier \
  --connections=10 \
  --method=POST \
  --header="Accept: application/json" \
  --header="Content-Type: application/json" \
  --body-file="./body.json" \
  --requests=100 \
  http://localhost:8080/visits

