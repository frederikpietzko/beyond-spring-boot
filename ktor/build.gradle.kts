plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  id("io.ktor.plugin")
  application
}

application {
  mainClass.set("io.ktor.server.cio.EngineMain")
}

val ktor_version: String by project
val exposedVersion: String by project

dependencies {
  implementation("org.jetbrains.exposed:exposed-core:0.59.0")
  implementation("org.jetbrains.exposed:exposed-jdbc:0.59.0")
  implementation("org.jetbrains.exposed:exposed-java-time:0.59.0")

  implementation("org.postgresql:postgresql:42.7.5")
  implementation("com.zaxxer:HikariCP:6.2.1")

  implementation("io.ktor:ktor-server-status-pages:$ktor_version")
  implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
  implementation("io.ktor:ktor-server-cio-jvm:$ktor_version")
  implementation("io.ktor:ktor-server-config-yaml:$ktor_version")
  implementation("io.ktor:ktor-server-content-negotiation:${ktor_version}")
  implementation("io.ktor:ktor-serialization-kotlinx-json:${ktor_version}")

  implementation("ch.qos.logback:logback-classic:1.5.18")
  implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
}
