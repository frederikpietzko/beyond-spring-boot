# Technical Plan: Helidon (MP & SE)

## Goal

Expose Prometheus metrics at `/metrics` with standard JVM and database instrumentation.

## Affected Modules

- `helidon-mp`
- `helidon-se`

## Changes

### 1. Helidon MP

#### Dependencies (`build.gradle.kts`)

Add the following dependency:

```kotlin
implementation("io.helidon.microprofile.metrics:helidon-microprofile-metrics")
```

Versioning is handled by the `helidon-dependencies` platform BOM.

#### Configuration

- The endpoint is automatically exposed at `/metrics`.

### 2. Helidon SE

#### Dependencies (`build.gradle.kts`)

Add the following dependencies:

```kotlin
implementation("io.helidon.webserver.observe:helidon-webserver-observe-metrics")
implementation("io.helidon.webserver.observe:helidon-webserver-observe-prometheus")
implementation("io.helidon.dbclient:helidon-dbclient-metrics")
```

#### Code Changes

Update the server routing to include observations:

```java
Server.builder()
    .observe(ObserveFeature.builder()
        .addObserver(MetricsObserver.create())
        .addObserver(PrometheusObserver.create())
        .build())
    .routing(it -> it.register("/metrics", PrometheusSupport.create())) // or similar for Helidon 4
    .build();
```

#### Database Metrics

The `helidon-dbclient-metrics` extension automatically collects metrics for DB calls if initialized correctly.

## Verification

1. Run the application: `./gradlew :helidon-mp:run` or `./gradlew :helidon-se:run`
2. Check metrics: `curl localhost:8080/metrics`
