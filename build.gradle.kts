plugins {
  id("io.quarkus") apply false
  kotlin("jvm") version "2.2.0" apply false
  kotlin("plugin.serialization") version "2.2.0" apply false
  id("io.ktor.plugin") version "3.2.3" apply false
  id("io.micronaut.application") version "4.5.4" apply false
  id("com.gradleup.shadow") version "8.3.7" apply false
  id("io.micronaut.aot") version "4.5.4" apply false
  id("org.kordamp.gradle.jandex") version "2.0.0" apply false
  id("org.hibernate.orm") version "7.1.0.Final" apply false
}

group = "com.github.frederikpietzko"
version = "1.0.0"

allprojects {
  group = "com.github.frederikpietzko"
  version = "1.0.0"

  repositories {
    mavenLocal()
    mavenCentral()
  }
}

