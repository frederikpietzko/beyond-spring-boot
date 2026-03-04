# Technical Plan: Quarkus & Quarkus JDBI

## Goal

Expose Prometheus metrics at `/metrics` for JVM, Hibernate, and Connection Pool.

## Affected Modules

- `quarkus`
- `quarkus-jdbi`

## Changes

### 1. Dependencies (`build.gradle.kts`)

Add the following dependency to the `dependencies` block of both modules:

```kotlin
implementation("io.quarkus:quarkus-micrometer-registry-prometheus")
```

Versions are managed by the Quarkus platform BOM.

### 2. Configuration (`src/main/resources/application.properties`)

Update properties to standardize the endpoint path:

```properties
quarkus.micrometer.export.prometheus.path=/metrics
```

Note: Quarkus automatically registers JVM and Hibernate/Hikari metrics if the extension is present.

### 3. Implementation Details

- **JVM Metrics**: Automatically enabled.
- **Connection Pool**: HikariCP metrics are automatically collected by Quarkus datasource management.

## Verification

1. Run the application: `./gradlew :quarkus:quarkusDev`
2. Check metrics: `curl localhost:8080/metrics`
3. Repeat for `quarkus-jdbi`.
