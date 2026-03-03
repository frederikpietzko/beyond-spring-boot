# Project Guidelines

Interview me bofore you start making changes until you are sure you are on the right track.

# Description

This is a simple project comparing different frameworks and libraries.
The same rest api is implemented in all frameworks.

Java 21 is used across the board. Some modules are implemented using Kotlin 2.2.0.

# Goals

Compare different frameworks and libraries using the same rest api.
The idea is to do the "naive" implementation of the rest api and highlight the differences and similarities.

- automate the provisioning of the benchmark environment in Azure using Terraform
- isolate the load generator from the application by using dedicated AKS node pools
- minimize network latency through private networking (VNet) between AKS and Postgres

# Run a specific module

- all modules can be run using gradle using the application plugin

# Non-goals

- do not introdise framework specific features that change api behaviour unless said otherwise
- do not introduce new features
- avoid benchmark optimizations
- do not collect logs or traces (metrics-only pipeline)
- no long-term persistence of the benchmark environment (ephemeral)

# API Contract

The REST API must remain consistent across modules. Every server must use the same port.

- same routes
- same json shapes
- same status codes

# Structure

The applications can be tested by running the loadtests in the loadtest directory as shell scripts.

# Testing

There are no tests, and there are none planned. For testing it is enough to execute the loadtests.

# Docker images

Docker images should be built and pushed to docker hub as public images in the frederikpietzko namespace.
DOCKERHUB_USERNAME and DOCKERHUB_PASSWORD are set as env variables in github actions.
Docker images should be built using gradle if possible using cloud native buildpacks, and Dockerfiles otherwise using a
distroless java base image.

# Repo structure

spring-boot: The original spring boot reference implementation, uses hibernate + jpa + spring data & spring mvc
spring-boot-jdbc: Uses spring data jdbc instead of spring data jpa
helidon-mp: Helidon Microprofile, uses hibernate + jpa
helidon-se: Helidon SE, the helidon reactive db client
http4k: Http4k implementation, uses exposed
javalin: javalin implementation, uses exposed and jackson
ktor: ktor implementation, uses exposed and kotlinx serialization
micronaut: micronaut implementation, uses hibernate + jpa and micronaut serialization
micronaut-jdbc: second micronaut implementation, uses jdbc with micronaut jdbc and micronaut serialization
quarkus: quarkus implementation, uses panache and jackson
quarkus-jdbi: quarkus implementation, uses jdbi and jackson
loadtest: Constains loadtest to run against port 8080 and k6 scripts for loadtests, runbook for testing in azure aks
pgbouncer: pgbouncer configuration
migration.sql: SQL file to create the database schema manually
infra: Contains terraform files to create the infrastructure in azure. You can find terraform output in
infra/.run/output.json

# Target Architecture

The benchmark infrastructure is designed to be ephemeral and consists of:
all resources should be created using terraform in Europe West. Do not create resources in other regions.
Check resource quotas in the subscription and adjust accordingly.

- AKS cluster with a system pool, an `apps` pool, and a `loadtest` pool (Standard_D8s_v5)
- Azure Database for PostgreSQL Flexible Server with private access in the VNet
- Private DNS linkage for internal resolution
- Metrics-only observability pipeline to Grafana Cloud
