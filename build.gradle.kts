plugins {
  id("io.quarkus") apply false
  id("io.micronaut.application") version "4.5.4" apply false
  id("com.gradleup.shadow") version "8.3.7" apply false
  id("io.micronaut.aot") version "4.5.4" apply false
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

