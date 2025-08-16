plugins {
  java
  application
}
val helidonVersion: String by project

java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
  implementation(enforcedPlatform("io.helidon:helidon-dependencies:$helidonVersion"))
  implementation("io.helidon.webserver:helidon-webserver")
  implementation("io.helidon.http.media:helidon-http-media-jsonp")

  annotationProcessor("org.projectlombok:lombok:1.18.38")
  compileOnly("org.projectlombok:lombok:1.18.38")

  implementation("org.slf4j:slf4j-api")
  implementation("io.helidon.logging:helidon-logging-slf4j")
  implementation("org.slf4j:jul-to-slf4j")
  implementation("ch.qos.logback:logback-classic:1.5.18")

  implementation("io.helidon.webserver.observe:helidon-webserver-observe-health")
  implementation("io.helidon.webserver.observe:helidon-webserver-observe-metrics")
  implementation("io.helidon.config:helidon-config-yaml")
  implementation("io.helidon.health:helidon-health-checks")

  testImplementation("io.helidon.webserver.testing.junit5:helidon-webserver-testing-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter")
  testImplementation("io.helidon.webclient:helidon-webclient")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
  mainClass = "com.github.frederikpietzko.Application"
}

val copyLibs = tasks.register<Copy>("copyLibs") {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
  dependsOn(tasks.jar)
  from(configurations.runtimeClasspath.get())
  into("${layout.buildDirectory.get()}/libs/libs")
}

tasks.assemble {
  dependsOn(copyLibs)
}

tasks.jar {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
  archiveFileName = "${project.name}.jar"
  manifest {
    attributes(
      "Main-Class" to application.mainClass.get(),
      "Class-Path" to configurations.runtimeClasspath.get().files.map { "libs/${it.name}" }.joinToString { " " },
    )
  }
}
