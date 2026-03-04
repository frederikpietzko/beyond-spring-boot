#!/usr/bin/env bash

bombardier \
  --connections=$1 \
  --method=GET \
  --header="Accept: application/json" \
  --requests=$2 \
  http://localhost:8080/visits

