# Technical Plan: Micronaut & Micronaut JDBC

## Goal

Expose Prometheus metrics at `/metrics` with standard JVM and connection pool metrics.

## Affected Modules

- `micronaut`
- `micronaut-jdbc`

## Changes

### 1. Dependencies (`build.gradle.kts`)

Add the following dependencies to the `dependencies` block of both modules:

```kotlin
implementation("io.micronaut.micrometer:micronaut-micrometer-registry-prometheus")
implementation("io.micronaut:micronaut-management")
```

Micronaut platform versions are used for these dependencies.

### 2. Configuration (`src/main/resources/application.properties`)

Standardize the prometheus path and expose the endpoint:

```properties
micronaut.metrics.export.prometheus.path=/metrics
endpoints.prometheus.enabled=true
endpoints.prometheus.sensitive=false
```

### 3. Implementation Details

- **JVM Metrics**: Automatically enabled by Micronaut's Micrometer support.
- **Connection Pool**: HikariCP metrics are automatically bound if the datasource is managed by Micronaut.

## Verification

1. Run the application: `./gradlew :micronaut:run`
2. Check metrics: `curl localhost:8080/metrics`
3. Repeat for `micronaut-jdbc`.
