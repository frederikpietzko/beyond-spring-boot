plugins {
  application
  kotlin("jvm")
  kotlin("plugin.serialization")
}

val exposedVersion: String by project

application {
  mainClass.set("com.github.frederikpietzko.ApplicationKt")
}

dependencies {
  implementation(platform("org.http4k:http4k-bom:6.15.1.0"))

  implementation("org.http4k:http4k-core")
  implementation("org.http4k:http4k-server-helidon")
  implementation("org.http4k:http4k-format-kotlinx-serialization")

  implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
  implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
  implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
  implementation("org.jetbrains.exposed:exposed-migration:$exposedVersion")
  runtimeOnly("org.postgresql:postgresql:42.7.7")
  implementation("ch.qos.logback:logback-classic:1.5.18")
  implementation("com.zaxxer:HikariCP:7.0.2")
}
