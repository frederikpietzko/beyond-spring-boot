plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  id("io.ktor.plugin")
  application
}

application {
  mainClass.set("io.ktor.server.cio.EngineMain")
}

kotlin {
  jvmToolchain(21)
}

tasks.register<Copy>("copyLibs") {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
  from(configurations.runtimeClasspath.get())
  into("${layout.buildDirectory.get()}/libs/libs")
}

tasks.assemble {
  dependsOn("copyLibs")
}

tasks.jar {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
  archiveFileName = "${project.name}.jar"
  manifest {
    attributes(
      "Main-Class" to application.mainClass.get(),
      "Class-Path" to configurations.runtimeClasspath.get().map { "libs/${it.name}" }.joinToString(" "),
    )
  }
}

tasks.register<Exec>("dockerBuild") {
  dependsOn("assemble")
  group = "docker"
  commandLine("docker", "build", "-t", "frederikpietzko/${project.name}:${project.version}", ".")
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
  implementation("io.ktor:ktor-server-metrics-micrometer:$ktor_version")
  implementation("io.micrometer:micrometer-registry-prometheus:1.16.3")

  implementation("ch.qos.logback:logback-classic:1.5.18")
  implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
}
