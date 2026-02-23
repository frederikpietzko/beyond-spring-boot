pluginManagement {
  val quarkusPluginVersion: String by settings
  val quarkusPluginId: String by settings
  repositories {
    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
  }
  plugins {
    id(quarkusPluginId) version quarkusPluginVersion
  }
}

rootProject.name = "beyond-spring-boot"
include(
  "spring-boot",
  "spring-boot-jdbc",
  "quarkus",
  "micronaut",
  "helidon-mp",
  "helidon-se",
  "javalin",
  "ktor",
  "http4k",
)
