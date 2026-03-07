plugins {
  application
  kotlin("jvm")
  kotlin("plugin.serialization")
}

val exposedVersion: String by project

kotlin {
  jvmToolchain(21)
}

application {
  mainClass.set("com.github.frederikpietzko.ApplicationKt")
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

dependencies {
  implementation(platform("org.http4k:http4k-bom:6.15.1.0"))

  implementation("org.http4k:http4k-core")
  implementation("org.http4k:http4k-server-helidon")
  implementation("org.http4k:http4k-format-kotlinx-serialization")

  implementation("org.jetbrains.exposed:exposed-core:0.59.0")
  implementation("org.jetbrains.exposed:exposed-jdbc:0.59.0")
  implementation("org.jetbrains.exposed:exposed-java-time:0.59.0")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.18.2")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")
  runtimeOnly("org.postgresql:postgresql:42.7.7")
  implementation("ch.qos.logback:logback-classic:1.5.18")
  implementation("com.zaxxer:HikariCP:7.0.2")
  implementation("io.micrometer:micrometer-registry-prometheus:1.16.3")
}
