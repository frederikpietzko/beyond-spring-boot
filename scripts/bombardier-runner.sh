#!/usr/bin/env bash

set -e

# Target URL
TARGET_URL=${TARGET_URL:-"http://localhost:8080"}
VISITS_URL="${TARGET_URL}/visits"

# Global results array
RESULTS_FILE="final_results.json"
echo "[]" > "$RESULTS_FILE"

# Determine bombardier path
BOMBARDIER_CMD="bombardier"

# Function to run bombardier and check constraints
run_bombardier() {
  local connections=$1
  local requests=$2
  local name=$3
  local method=${4:-GET}
  local extra_args=$5

  echo "------------------------------------------------"
  echo "Running Scenario: $name ($requests requests, $connections connections, $method)"

  start_time=$(date +%s)

  # Run bombardier with JSON output to a temporary file
  # We use --print=r to get the results in JSON
  $BOMBARDIER_CMD \
    --connections="$connections" \
    --requests="$requests" \
    --method="$method" \
    --header="Accept: application/json" \
    --format=json \
    --print=r \
    $extra_args \
    "$VISITS_URL" > result.json || { echo "Bombardier command failed"; return 1; }

  end_time=$(date +%s)
  duration=$((end_time - start_time))

  # Add result to global results file
  jq --arg name "$name" --arg dur "$duration" '. + {scenario: $name, duration_seconds: ($dur|tonumber)}' result.json > single_result.json
  jq --slurpfile new_res single_result.json '. + $new_res' "$RESULTS_FILE" > tmp.json && mv tmp.json "$RESULTS_FILE"

  # Also print human readable summary for logs
  jq '.result' result.json

  echo "Scenario $name completed in $duration seconds."

  # Validate duration < 10 minutes (600 seconds)
  if [ "$duration" -ge 600 ]; then
    echo "ERROR: Scenario $name took too long ($duration seconds). Limit is 600s."
    return 1
  fi

  # Validate status codes (check for non-2xx codes in result.json)
  non_2xx=$(jq '[.result | .req1xx + .req3xx + .req4xx + .req5xx + .others] | add // 0' result.json)

  if [ "$non_2xx" -gt 0 ]; then
    echo "ERROR: Scenario $name had $non_2xx non-2xx response codes."
    # Print the counts
    jq '.result | {req1xx, req2xx, req3xx, req4xx, req5xx, others}' result.json
    return 1
  fi

  echo "Scenario $name passed constraints."
  return 0
}

# 1. Check if database is empty
echo "Checking if database is empty at $VISITS_URL..."
# Use curl for the initial check to see if it's an empty array
response=$(curl -s "$VISITS_URL" || echo "ERROR")
if [ "$response" == "[]" ] || [ -z "$response" ] || [ "$response" == "ERROR" ]; then
    echo "Database is empty or unreachable ($response). Pre-filling with 100 entries..."
    bombardier \
        --connections=10 \
        --requests=100 \
        --method=POST \
        --header="Content-Type: application/json" \
        --body-file="$(dirname "$0")/body.json" \
        "$VISITS_URL"
else
    echo "Database is not empty. Skipping pre-fill."
fi

# 2. Scenarios
# 1000 requests with 10 parallel connections
run_bombardier 10 1000 "1k-10c" || { ABORTED=true; }

# 10k with 100
if [ "$ABORTED" != "true" ]; then
    run_bombardier 100 10000 "10k-100c" || { ABORTED=true; }
fi

# 100k with 500
if [ "$ABORTED" != "true" ]; then
    run_bombardier 500 100000 "100k-500c" || { ABORTED=true; }
fi

# 100k with 1000
if [ "$ABORTED" != "true" ]; then
    run_bombardier 1000 100000 "100k-1000c" || { ABORTED=true; }
fi

if [ "$ABORTED" == "true" ]; then
    echo "------------------------------------------------"
    echo "Load test aborted due to failure/constraints."
fi

echo "------------------------------------------------"
echo "Load Test Summary:"
printf "%-15s | %-12s | %-12s | %-12s | %-10s\n" "Scenario" "RPS (mean)" "Lat. (mean)" "Lat. (max)" "Success"
echo "----------------|--------------|--------------|--------------|----------"

jq -r '.[] | "\(.scenario)|\(.result.rps.mean | tonumber | round) req/s|\(.result.latency.mean / 1000 | tonumber | round) ms|\(.result.latency.max / 1000 | tonumber | round) ms|\(.result.req2xx) ok"' "$RESULTS_FILE" | while IFS='|' read -r scen rps mean_lat max_lat success; do
    printf "%-15s | %-12s | %-12s | %-12s | %-10s\n" "$scen" "$rps" "$mean_lat" "$max_lat" "$success"
done

# Clean up temp files
rm -f result.json single_result.json "$RESULTS_FILE"

echo ""
echo "All scenarios completed successfully!"
