# Runbook: Ephemeral AKS (VNet) + Managed Postgres (Private) + k6-operator (k6 Cloud) + Metrics-only

## 0) Install tools (once)

- Install: Azure CLI (`az`), Terraform, kubectl, Helm
- Verify:
    - `az version`
    - `terraform -version`
    - `kubectl version --client`
    - `helm version`

## 1) Terraform access to Azure (local, no CI)

1. Login:
    - `az login`
2. Select subscription:
    - `az account list -o table`
    - `az account set --subscription "<SUBSCRIPTION_ID>"`

## 2) Terraform: provision ephemeral infrastructure (local state)

Goal: lower latency + consistent results, while keeping outbound internet for k6 Cloud.

1. `terraform init`
2. `terraform apply`

Your Terraform should create:

- VNet + subnets
- AKS cluster (with outbound internet via standard LB outbound)
- 2 AKS node pools (important: isolate loadgen from app)
    - apps pool: `Standard_D8s_v5`, 1 node
    - loadtest pool: `Standard_D8s_v5`, start with 2 nodes (scale to 3 if k6 becomes the limiter)
- Azure Database for PostgreSQL Flexible Server with PRIVATE access in the VNet (fast/consistent latency)
- Private DNS linkage so AKS pods can resolve Postgres privately

Keep Terraform outputs handy:

- AKS name + resource group
- Postgres host/port/db/user/password (use placeholders; don’t commit secrets)

## 3) Connect kubectl to AKS

- `az aks get-credentials --resource-group "<RG>" --name "<AKS_NAME>" --overwrite-existing`
- `kubectl get nodes`

## 4) Namespaces

- `kubectl create namespace apps`
- `kubectl create namespace loadtest`
- `kubectl create namespace observability`

## 5) Install k6-operator (Helm) and k6 Cloud token

1. Install k6-operator into `loadtest` namespace (Helm)
2. Create a Kubernetes Secret in `loadtest` containing your k6 Cloud token

## 6) Install metrics-only pipeline to Grafana Cloud (no logs, no traces)

Goal: JVM + pool + minimal K8s metrics to Grafana Cloud Metrics (stay free-tier friendly).

1. Install a metrics collector via Helm (Grafana Alloy/Agent is a good default)
2. Configure it to:
    - scrape only what you need (apps namespace + minimal cluster metrics)
    - remote_write to Grafana Cloud Metrics
3. Keep cardinality low:
    - avoid per-path/per-id labels
    - avoid scraping “everything Kubernetes” by default

## 7) Add and expose metrics for ALL services (JVM + connection pool)

For each service you deploy:

1. Enable a Prometheus-style `/metrics` endpoint
2. Ensure it includes:
    - JVM metrics (heap, GC, threads, CPU)
    - connection pool metrics (e.g., Hikari active/idle/pending)
3. Make sure the collector can scrape it (labels/annotations or ServiceMonitor-style integration, depending on your
   collector)
4. Verify from inside cluster:
    - run a temporary curl pod in `loadtest` and fetch:
        - `http://<svc>.apps.svc.cluster.local:8080/metrics`

## 8) Deploy ONE app at a time (Helm)

When running a benchmark:

1. Deploy exactly one app into `apps`
2. Configure DB connection to the managed Postgres private hostname
3. Set resources for the app pod:
    - requests/limits: 2 CPU, 4Gi RAM
4. Pin scheduling to the apps node pool (nodeSelector/affinity)
5. Confirm it’s reachable internally:
    - `curl http://<svc>.apps.svc.cluster.local:8080/...`

## 9) Ensure k6 and app never compete for nodes

- Apps workloads: pinned to apps node pool
- k6 workloads (operator runners): pinned to loadtest node pool
- Validate:
    - `kubectl -n apps get pods -o wide`
    - `kubectl -n loadtest get pods -o wide`

## 10) Run distributed k6 tests (one endpoint per run)

General approach:

1. Do a small smoke run (low VUs) to confirm correctness + k6 Cloud reporting
2. Scale distributed execution until either:
    - the app hits its limit, or
    - you discover k6 is the limiter (then add k6 instances / nodes)

Starting points (tune per endpoint):

- Small payload endpoints (GET by id, POST):
    - start with 8 k6 instances
    - then 12 → 16 if needed
    - if k6 is still the limiter at 16, scale loadtest pool from 2 nodes → 3 nodes and retry
- Larger response endpoint (GET list of 100):
    - start with 4–8 k6 instances

Target URL pattern (internal only):

- `http://<svc>.apps.svc.cluster.local:8080/<endpoint>`

## 11) Observe during load (Grafana Cloud + k6 Cloud)

- k6 Cloud: throughput, latency percentiles, errors
- Grafana Cloud Metrics: JVM + pool + CPU/mem
- If you approach free-tier limits:
    - reduce scrape scope
    - drop noisy metric families
    - reduce label cardinality

## 12) Cleanup (ephemeral teardown)

1. Uninstall Helm releases (optional; not required if destroying the cluster)
2. `terraform destroy`
