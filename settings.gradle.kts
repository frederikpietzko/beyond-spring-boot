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
  "quarkus",
  "micronaut",
  "helidon-mp",
  "helidon-se",
  "javalin",
  "ktor",
)
