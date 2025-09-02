#!/usr/bin/env bash

bombardier \
  --connections=100 \
  --method=GET \
  --header="Accept: application/json" \
  --requests=100000 \
  http://localhost:8080/visits

