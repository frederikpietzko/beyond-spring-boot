# Metrics Implementation Plan

This document outlines the plan for adding Prometheus metrics endpoints to all REST API modules in this project. The
goal is to provide consistent JVM and connection pool metrics using Micrometer where possible.

## General Strategy

- Use **Micrometer** as the instrumentation library for all JVM-based modules.
- Expose a `/metrics` endpoint in Prometheus format.
- Ensure **JVM metrics** (CPU, Memory, GC, Threads) are included.
- Ensure **Connection Pool metrics** (HikariCP, etc.) are included.
- Security is ignored as per requirements (public `/metrics` endpoint).
- Commit between each successful module implementation. Test each module using curl.
- **Do not** add any other metrics libraries.
- Helm Chart for Apps is not implemented yet and out of scope. Will be added later.
- Work on a single module at a time.
- Work on a seperate branch, but include all modules in it.

---

## Module Specific Plans

### 1. Spring Boot & Spring Boot JDBC

- **Action**: Add `spring-boot-starter-actuator` and `micrometer-registry-prometheus`.
- **Configuration**:
    - `management.endpoints.web.exposure.include=prometheus`
    - `management.endpoint.metrics.enabled=true`
    - `management.prometheus.metrics.export.enabled=true`
- **Default Path**: `/actuator/prometheus` (Consider mapping to `/metrics` for consistency across all modules).

### 2. Quarkus & Quarkus JDBI

- **Action**: Add `io.quarkus:quarkus-micrometer-registry-prometheus` extension.
- **Configuration**:
    - Quarkus automatically enables JVM and Hibernate/Hikari metrics when the extension is present.
- **Default Path**: `/q/metrics` (Prometheus format). Consider mapping to `/metrics` for consistency across all modules.

### 3. Micronaut & Micronaut JDBC

- **Action**: Add `io.micronaut.micrometer:micronaut-micrometer-registry-prometheus` and
  `io.micronaut:micronaut-management`.
- **Configuration**:
    - `endpoints.prometheus.enabled=true`
    - `endpoints.prometheus.sensitive=false`
- **Default Path**: `/prometheus`. Consider mapping to `/metrics` for consistency across all modules.

### 4. Ktor

- **Action**: Add `io.ktor:ktor-server-metrics-micrometer` and `io.micrometer:micrometer-registry-prometheus`.
- **Implementation**:
    - Install `MicrometerMetrics` plugin in the Ktor application.
    - Create a `PrometheusMeterRegistry`.
    - Add a route `get("/metrics") { call.respond(registry.scrape()) }`.

### 5. Helidon (MP & SE)

- **Helidon MP**: Add `helidon-microprofile-metrics`. It provides `/metrics` automatically.
- **Helidon SE**: Add `helidon-webserver-prometheus`.
    - Register `PrometheusSupport` in the routing.
    - Ensure `helidon-dbclient-metrics` is included for DB metrics.
- Helidon has less priority, do last.

### 6. Javalin / http4k

- **Action**: Add `io.micrometer:micrometer-registry-prometheus`.
- **Implementation**:
    - Initialize `PrometheusMeterRegistry`.
    - Bind `ClassLoaderMetrics`, `JvmMemoryMetrics`, `JvmGcMetrics`, `ProcessorMetrics`, `JvmThreadMetrics`.
    - Bind Hikari pool to the registry using `hikariDataSource.setMetricRegistry(registry)`.
    - Add a GET `/metrics` endpoint that returns `registry.scrape()`.

---

## Observability Integration (Alloy)

- The Grafana Alloy is already configured to scrape pods in the `apps` namespace.
- Ensure each Kubernetes Service or Pod has the necessary annotations if Alloy's `discovery.kubernetes` requires them (
  e.g., `prometheus.io/scrape: "true"` and `prometheus.io/port: "8080"`).
- Since different frameworks use different paths (e.g., `/actuator/prometheus`, `/q/metrics`, `/metrics`), we should
  ideally standardize all to `/metrics` or update Alloy's relabel_configs to handle path discovery.

## Next Steps

1. Add dependencies to `build.gradle.kts` files.
2. Update application configurations/code to expose the registry.
3. Verify by running one module and checking `curl localhost:8080/metrics`.
