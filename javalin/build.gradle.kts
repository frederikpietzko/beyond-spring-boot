plugins {
  kotlin("jvm")
}

val exposedVersion = "1.0.0-beta-5"

dependencies {
  implementation("io.javalin:javalin:6.7.0")
  implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
  implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
  implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
  implementation("org.jetbrains.exposed:exposed-migration:$exposedVersion")
  runtimeOnly("org.postgresql:postgresql:42.7.7")
  implementation("ch.qos.logback:logback-classic:1.5.18")
  implementation("com.zaxxer:HikariCP:7.0.2")
}
