# Technical Plan: Spring Boot & Spring Boot JDBC

## Goal

Expose Prometheus metrics at `/metrics` with standard JVM and HikariCP instrumentation.

## Affected Modules

- `spring-boot`
- `spring-boot-jdbc`

## Changes

### 1. Dependencies (`build.gradle.kts`)

Add the following dependencies to the `dependencies` block of both modules:

```kotlin
implementation("org.springframework.boot:spring-boot-starter-actuator")
implementation("io.micrometer:micrometer-registry-prometheus")
```

Since both modules use the Spring Boot plugin, versioning is handled by the platform BOM.

### 2. Configuration (`src/main/resources/application.yaml`)

Update configuration to expose and rename the Prometheus endpoint:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: prometheus
      base-path: /
      path-mapping:
        prometheus: metrics
  endpoint:
    metrics:
      enabled: true
    prometheus:
      enabled: true
  prometheus:
    metrics:
      export:
        enabled: true
```

This configuration:

- Standardizes the base path to `/` (instead of `/actuator`).
- Maps `prometheus` to `/metrics`.
- Ensures all metrics are enabled.

### 3. Implementation Details

- **JVM Metrics**: Automatically enabled by Spring Boot Actuator.
- **Connection Pool**: HikariCP metrics are automatically bound if the datasource is initialized through Spring's
  auto-configuration.

## Verification

1. Run the application: `./gradlew :spring-boot:run`
2. Check metrics: `curl localhost:8080/metrics`
3. Repeat for `spring-boot-jdbc`.
