# Technical Plan: Javalin & http4k

## Goal

Manually integrate Micrometer into Javalin and http4k modules and expose Prometheus metrics at `/metrics`.

## Affected Modules

- `javalin`
- `http4k`

## Changes

### 1. Dependencies (`build.gradle.kts`)

Add the following dependency:

```kotlin
implementation("io.micrometer:micrometer-registry-prometheus:1.16.3")
```

### 2. Code Changes

#### Registry Initialization

Shared across both modules:

```kotlin
val prometheusRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT).apply {
    config().commonTags("application", "your-app-name")
    ClassLoaderMetrics().bindTo(this)
    JvmMemoryMetrics().bindTo(this)
    JvmGcMetrics().bindTo(this)
    ProcessorMetrics().bindTo(this)
    JvmThreadMetrics().bindTo(this)
}
```

#### Hikari Integration

Bind the datasource to the registry:

```kotlin
hikariDataSource.metricRegistry = prometheusRegistry
```

#### Javalin Endpoint

```kotlin
app.get("/metrics") { ctx ->
    ctx.contentType("text/plain").result(prometheusRegistry.scrape())
}
```

#### http4k Endpoint

```kotlin
val metricsRoute = "/metrics" bind Method.GET to {
    Response(Status.OK).body(prometheusRegistry.scrape())
}
```

## Verification

1. Run the application: `./gradlew :javalin:run` or `./gradlew :http4k:run`
2. Check metrics: `curl localhost:8080/metrics`
