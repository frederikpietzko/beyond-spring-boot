# Technical Plan: Ktor

## Goal

Manually integrate Micrometer Prometheus metrics into the Ktor application and expose them at `/metrics`.

## Affected Module

- `ktor`

## Changes

### 1. Dependencies (`build.gradle.kts`)

Add the following dependencies:

```kotlin
implementation("io.ktor:ktor-server-metrics-micrometer:$ktor_version")
implementation("io.micrometer:micrometer-registry-prometheus:1.16.3")
```

### 2. Code Changes

#### Update `App.kt`

Install the `MicrometerMetrics` plugin in `Application.main`:

```kotlin
import io.ktor.server.metrics.micrometer.*
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry

val appMicrometerRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

suspend fun Application.main() {
    install(MicrometerMetrics) {
        registry = appMicrometerRegistry
        meterBinders = listOf(
            JvmMemoryMetrics(),
            JvmGcMetrics(),
            ProcessorMetrics()
        )
    }
    setupDatabase()
    setupSerialization()
    setupRouting()
}
```

#### Update `Routing.kt`

Add the `/metrics` endpoint:

```kotlin
routing {
    get("/metrics") {
        call.respond(appMicrometerRegistry.scrape())
    }
    // existing routes...
}
```

#### Update `Infrastructure.kt` (if applicable)

Bind the HikariDataSource to the registry in the database setup code:

```kotlin
HikariDataSource(config).apply {
    metricRegistry = appMicrometerRegistry
}
```

## Verification

1. Run the application: `./gradlew :ktor:run`
2. Check metrics: `curl localhost:8080/metrics`
