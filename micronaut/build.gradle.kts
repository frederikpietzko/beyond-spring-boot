plugins {
  java
  id("io.micronaut.application")
  id("com.gradleup.shadow")
  id("io.micronaut.aot")
}

dependencies {
  annotationProcessor("io.micronaut.jaxrs:micronaut-jaxrs-processor")
  annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
  annotationProcessor("org.projectlombok:lombok:1.18.38")
  annotationProcessor("io.micronaut.tracing:micronaut-tracing-opentelemetry-annotation")
  implementation("io.opentelemetry:opentelemetry-exporter-otlp")
  implementation("io.opentelemetry:opentelemetry-exporter-logging")
  implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry-http")
  implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry-jdbc")
  implementation("io.micronaut.micrometer:micronaut-micrometer-core")
  implementation("io.micronaut.micrometer:micronaut-micrometer-registry-otlp")
  compileOnly("org.projectlombok:lombok:1.18.38")
  implementation("io.micronaut.beanvalidation:micronaut-hibernate-validator")
  implementation("io.micronaut:micronaut-http-server-netty")
  implementation("io.micronaut.jaxrs:micronaut-jaxrs-server")
  implementation("io.micronaut.serde:micronaut-serde-jackson")
  compileOnly("io.micronaut:micronaut-http-client")
  runtimeOnly("ch.qos.logback:logback-classic")
  implementation("io.micronaut.sql:micronaut-hibernate-jpa")
  implementation("io.micronaut.data:micronaut-data-tx-hibernate")
  implementation("io.micronaut.sql:micronaut-jdbc-hikari")
  runtimeOnly("org.postgresql:postgresql")
  testImplementation("io.micronaut:micronaut-http-client")
}


application {
  mainClass = "com.github.frederikpietzko.Application"
}

java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}


graalvmNative.toolchainDetection = false

micronaut {
  runtime("http_poja")
  testRuntime("junit5")
  processing {
    incremental(true)
    annotations("com.github.frederikpietzko.*")
  }
  aot {
    // Please review carefully the optimizations enabled below
    // Check https://micronaut-projects.github.io/micronaut-aot/latest/guide/ for more details
    optimizeServiceLoading = false
    convertYamlToJava = false
    precomputeOperations = true
    cacheEnvironment = true
    optimizeClassLoading = true
    deduceEnvironment = true
    optimizeNetty = true
    replaceLogbackXml = true
  }
}


tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
  jdkVersion = "21"
}


