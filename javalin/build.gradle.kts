plugins {
  kotlin("jvm")
  application
}

val exposedVersion: String by project

application {
  mainClass.set("com.github.frederikpietzko.AppKt")
}

dependencies {
  implementation("io.javalin:javalin:6.7.0")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.2")
  implementation("org.jetbrains.exposed:exposed-core:0.59.0")
  implementation("org.jetbrains.exposed:exposed-jdbc:0.59.0")
  implementation("org.jetbrains.exposed:exposed-java-time:0.59.0")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.18.2")
  runtimeOnly("org.postgresql:postgresql:42.7.7")
  implementation("ch.qos.logback:logback-classic:1.5.18")
  implementation("com.zaxxer:HikariCP:7.0.2")
}
