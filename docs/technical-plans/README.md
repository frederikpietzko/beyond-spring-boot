# Concrete Technical Plans for Metrics Implementation

This directory contains detailed technical plans for each module to implement Prometheus metrics using Micrometer.

## Modules

- [Spring Boot & Spring Boot JDBC](spring-boot.md)
- [Quarkus & Quarkus JDBI](quarkus.md)
- [Micronaut & Micronaut JDBC](micronaut.md)
- [Ktor](ktor.md)
- [Helidon (MP & SE)](helidon.md)
- [Javalin & http4k](javalin-http4k.md)

## Common Requirements

- All modules must expose Prometheus metrics at `/metrics`.
- JVM metrics (CPU, Memory, GC, Threads) must be included.
- HikariCP connection pool metrics must be included.
- Port 8080 is used for all modules.
