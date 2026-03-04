#!/bin/bash

# Exit immediately if a command exits with a non-zero status.
set -e

# Always run from the project root
SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
cd "$SCRIPT_DIR/.."

# Default values
VUS=""
DURATION=""
PARALLELISM=""
STOP=false

# Usage information
usage() {
  echo "Usage: $0 [options] <test-name>"
  echo ""
  echo "Available tests:"
  echo "  get-all-visits"
  echo "  get-visit"
  echo "  create-visit"
  echo ""
  echo "Options:"
  echo "  -u, --vus <number>         Set the number of virtual users (MAX_VUS)"
  echo "  -d, --duration <string>    Set the duration (e.g., 5m, 1h)"
  echo "  -p, --parallelism <number> Set the number of k6 runner instances"
  echo "  -s, --stop                 Stop the specified loadtest"
  echo "  -h, --help                 Show this help message"
  echo ""
  echo "Example:"
  echo "  $0 -u 50 -d 15m get-all-visits"
  echo "  $0 --stop get-all-visits"
  exit 1
}

# Parse options
while [[ $# -gt 0 ]]; do
  case $1 in
    -u|--vus)
      VUS="$2"
      shift 2
      ;;
    -d|--duration)
      DURATION="$2"
      shift 2
      ;;
    -p|--parallelism)
      PARALLELISM="$2"
      shift 2
      ;;
    -s|--stop)
      STOP=true
      shift
      ;;
    -h|--help)
      usage
      ;;
    -*)
      echo "Unknown option: $1"
      usage
      ;;
    *)
      TEST_NAME="$1"
      shift
      ;;
  esac
done

if [ -z "$TEST_NAME" ]; then
  usage
fi

# Validate test name
case $TEST_NAME in
  get-all-visits|get-visit|create-visit)
    ;;
  *)
    echo "Error: Unknown test '$TEST_NAME'"
    usage
    ;;
esac

NAMESPACE="loadtest"
RELEASE_NAME="benchmark-loadtest"
CHART_PATH="charts/loadtest"

if [ "$STOP" = true ]; then
  echo "Stopping loadtest: $TEST_NAME..."
  helm upgrade --install "$RELEASE_NAME" "$CHART_PATH" \
    --namespace "$NAMESPACE" \
    --create-namespace \
    --set "loadtests.$TEST_NAME.enabled=false" \
    --reuse-values
  echo "Loadtest $TEST_NAME stopped."
else
  echo "Starting loadtest: $TEST_NAME..."

  HELM_OPTS=("--set" "loadtests.$TEST_NAME.enabled=true")

  if [ -n "$VUS" ]; then
    HELM_OPTS+=("--set" "loadtests.$TEST_NAME.env.MAX_VUS=$VUS")
  fi

  if [ -n "$DURATION" ]; then
    HELM_OPTS+=("--set" "loadtests.$TEST_NAME.env.DURATION=$DURATION")
  fi

  if [ -n "$PARALLELISM" ]; then
    HELM_OPTS+=("--set" "loadtests.$TEST_NAME.parallelism=$PARALLELISM")
  fi

  helm upgrade --install "$RELEASE_NAME" "$CHART_PATH" \
    --namespace "$NAMESPACE" \
    --create-namespace \
    "${HELM_OPTS[@]}" \
    --reuse-values

  echo "Successfully started $TEST_NAME."
  echo "You can check the status with: kubectl get testruns -n $NAMESPACE"
fi
