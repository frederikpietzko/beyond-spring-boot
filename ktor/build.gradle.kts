plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  id("io.ktor.plugin")
  application
}

application {
  mainClass.set("io.ktor.server.netty.EngineMain")
}

val ktor_version: String by project
val exposedVersion: String by project

dependencies {
  implementation("io.ktor:ktor-server-status-pages:$ktor_version")
  implementation("io.ktor:ktor-server-core-jvm")
  implementation("io.ktor:ktor-server-netty-jvm")
  implementation("ch.qos.logback:logback-classic:1.5.18")
  implementation("io.ktor:ktor-server-config-yaml:$ktor_version")

  implementation("org.jetbrains.exposed:exposed-core:${exposedVersion}")
  implementation("org.jetbrains.exposed:exposed-r2dbc:${exposedVersion}")
  implementation("org.jetbrains.exposed:exposed-java-time:${exposedVersion}")
  implementation("org.jetbrains.exposed:exposed-migration:${exposedVersion}")

  implementation("org.postgresql:r2dbc-postgresql:1.0.7.RELEASE")
  implementation("io.ktor:ktor-server-core:${ktor_version}")
  implementation("io.ktor:ktor-server-content-negotiation:${ktor_version}")
  implementation("io.ktor:ktor-server-core:${ktor_version}")
  implementation("io.ktor:ktor-serialization-kotlinx-json:${ktor_version}")
  implementation("io.ktor:ktor-server-content-negotiation:${ktor_version}")
  implementation("io.ktor:ktor-server-core:${ktor_version}")

  implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
}
