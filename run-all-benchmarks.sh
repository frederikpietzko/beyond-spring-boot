#!/usr/bin/env bash

# Exit on error
set -e

COMMIT_HASH=$1

if [ -z "$COMMIT_HASH" ]; then
    echo "Usage: $0 <commit-hash>"
    exit 1
fi

APPS_NAMESPACE="apps"
LOADTEST_NAMESPACE="loadtest"
APPS_CHART="charts/apps"
BOMBARDIER_CHART="charts/bombardier-runner"

# List of apps to benchmark (derived from values files)
APPS=(
    "helidon-mp"
    "helidon-se"
    "http4k"
    "javalin"
    "ktor"
    "micronaut-jdbc"
    "micronaut"
    "quarkus-jdbi"
    "quarkus"
    "spring-boot-jdbc"
    "spring-boot"
)

echo "Starting benchmark run for commit: $COMMIT_HASH"

for APP in "${APPS[@]}"; do
    echo "=========================================================="
    echo "Benchmarking: $APP"
    echo "=========================================================="

    VALUES_FILE="charts/apps/values-$APP.yaml"

    # 1. Deploy the app
    echo "Deploying $APP..."
    helm upgrade --install benchmark-app "$APPS_CHART" \
        --namespace "$APPS_NAMESPACE" \
        --create-namespace \
        -f "$VALUES_FILE" \
        --set image.tag="$COMMIT_HASH" \
        --wait --timeout 5m

    echo "App $APP deployed. Waiting 10 seconds for stability..."
    sleep 10

    # 1.1 Collect App Startup Time
    APP_POD=$(kubectl get pods -n "$APPS_NAMESPACE" -l app.kubernetes.io/name=apps -o jsonpath='{.items[0].metadata.name}')
    echo "Collecting startup metrics for pod $APP_POD..."

    # Try to find common startup patterns:
    # Spring Boot: "Started ... in ... seconds"
    # Quarkus: "Started in ...s"
    # Micronaut: "Started in ...ms"
    # Helidon: "Started in ... ms"
    # Ktor: "Responding at http://0.0.0.0:8080"
    STARTUP_LOG=$(kubectl logs -n "$APPS_NAMESPACE" "$APP_POD" | grep -Ei "Started in|Started .* in|Responding at" | tail -n 1 || true)

    # 2. Ensure previous job is cleaned up
    echo "Cleaning up previous benchmark jobs..."
    helm uninstall bombardier-runner --namespace "$LOADTEST_NAMESPACE" || true
    kubectl delete job -n "$LOADTEST_NAMESPACE" -l app.kubernetes.io/name=bombardier-runner || true

    # 3. Run the benchmark
    echo "Starting bombardier-runner for $APP..."
    # We run it in background to collect metrics during run, but wait, helm upgrade --wait will block.
    # So we better start it, and then collect metrics in a loop or just take a snapshot during the run.
    helm upgrade --install bombardier-runner "$BOMBARDIER_CHART" \
        --namespace "$LOADTEST_NAMESPACE" \
        --create-namespace \
        --wait --timeout 15m &
    HELM_PID=$!

    # Wait a bit for the load to start
    # We take multiple samples during the test to get a better picture of peak usage
    echo "Capturing CPU/Memory usage during benchmark..."
    RESOURCES_USAGE=""
    for i in {1..5}; do
        sleep 60
        SAMPLE=$(kubectl top pod "$APP_POD" -n "$APPS_NAMESPACE" --no-headers 2>/dev/null || echo "Metric not available")
        RESOURCES_USAGE="${RESOURCES_USAGE}\nSample $i: $SAMPLE"
    done

    # Wait for helm to finish
    wait $HELM_PID || true

    # 4. Collect logs
    LOG_FILE="benchmark-$APP-$COMMIT_HASH.log"
    echo "Collecting logs into $LOG_FILE..."

    {
        echo "=========================================================="
        echo "APP: $APP"
        echo "COMMIT: $COMMIT_HASH"
        echo "STARTUP LOG: $STARTUP_LOG"
        echo -e "RESOURCES USAGE (SAMPLES): $RESOURCES_USAGE"
        echo "=========================================================="
        echo ""
        echo "--- Bombardier Runner Logs ---"
        kubectl logs -l app.kubernetes.io/name=bombardier-runner -n "$LOADTEST_NAMESPACE"
        echo ""
        echo "--- Application Logs (Tail) ---"
        kubectl logs -n "$APPS_NAMESPACE" "$APP_POD" --tail=100
    } > "$LOG_FILE"

    echo "Finished benchmarking $APP. Waiting 10 seconds before next test..."
    sleep 10
done

echo "=========================================================="
echo "All benchmarks completed for commit $COMMIT_HASH"
echo "=========================================================="
