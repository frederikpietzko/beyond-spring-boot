#!/usr/bin/env bash

bombardier \
  --connections=100 \
  --method=POST \
  --header="Accept: application/json" \
  --header="Content-Type: application/json" \
  --body-file=./body.json \
  --requests=100 \
  http://localhost:8080/visits

