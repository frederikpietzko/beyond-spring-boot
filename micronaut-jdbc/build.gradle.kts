import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import io.micronaut.gradle.docker.MicronautDockerfile
import io.micronaut.gradle.docker.NativeImageDockerfile

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
  annotationProcessor("io.micronaut.data:micronaut-data-processor")
  compileOnly("org.projectlombok:lombok:1.18.38")
  implementation("io.micronaut.beanvalidation:micronaut-hibernate-validator")
  implementation("io.micronaut:micronaut-http-server-netty")
  implementation("io.micronaut.jaxrs:micronaut-jaxrs-server")
  implementation("io.micronaut.serde:micronaut-serde-jackson")
  compileOnly("io.micronaut:micronaut-http-client")
  runtimeOnly("ch.qos.logback:logback-classic")
  implementation("io.micronaut.data:micronaut-data-jdbc")
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

tasks.named<MicronautDockerfile>("dockerfile") {
  baseImage.set("gcr.io/distroless/java21-debian12")
  exposedPorts.set(listOf(8080))
}

tasks.named<DockerBuildImage>("dockerBuild") {
  images.add("frederikpietzko/${project.name}:${project.version}")
}

tasks.named<NativeImageDockerfile>("dockerfileNative") {
  jdkVersion = "21"
}


