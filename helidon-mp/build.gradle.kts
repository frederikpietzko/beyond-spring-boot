plugins {
  java
  application
  id("org.kordamp.gradle.jandex")
  id("org.hibernate.orm")
}

val helidonVersion: String by project

hibernate {
  enhancement {
    enableDirtyTracking = true
    enableLazyInitialization = true
    enableAssociationManagement = true
  }
}

dependencies {
  implementation(enforcedPlatform("io.helidon:helidon-dependencies:$helidonVersion"))
  implementation("io.helidon.microprofile.bundles:helidon-microprofile")
  implementation("org.glassfish.jersey.media:jersey-media-json-binding")
  implementation("org.slf4j:slf4j-api")
  implementation("io.helidon.logging:helidon-logging-slf4j")
  implementation("org.slf4j:jul-to-slf4j")
  implementation("ch.qos.logback:logback-classic:1.5.18")
  runtimeOnly("io.helidon.integrations.cdi:helidon-integrations-cdi-datasource-hikaricp")
  runtimeOnly("io.helidon.integrations.db:helidon-integrations-db-pgsql")
  runtimeOnly("org.postgresql:postgresql")

  implementation("jakarta.persistence:jakarta.persistence-api")
  runtimeOnly("io.helidon.integrations.cdi:helidon-integrations-cdi-jpa")
  implementation("jakarta.transaction:jakarta.transaction-api")
  implementation("io.helidon.integrations.cdi:helidon-integrations-cdi-jta-weld")
  implementation("io.helidon.integrations.cdi:helidon-integrations-cdi-hibernate")
  annotationProcessor("org.hibernate.orm:hibernate-processor:7.1.0.Final")

  annotationProcessor("org.projectlombok:lombok:1.18.38")
  compileOnly("org.projectlombok:lombok:1.18.38")

  runtimeOnly("io.smallrye:jandex")
  runtimeOnly("jakarta.activation:jakarta.activation-api")

  testCompileOnly("org.junit.jupiter:junit-jupiter-api")
}

application {
  mainClass = "io.helidon.microprofile.cdi.Main"
}

java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
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

tasks.distTar {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.distZip {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.jar {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
  dependsOn(tasks.jandex)
  archiveFileName = "${project.name}.jar"
  manifest {
    attributes(
      "Main-Class" to application.mainClass.get(),
      "Class-Path" to configurations.runtimeClasspath.get().files.map { "libs/${it.name}" }.joinToString { " " },
    )
  }
}

val copyBeansXML = tasks.register<Copy>("moveBeansXML") {
    from("${layout.buildDirectory.get()}/resources/main/META-INF/beans.xml")
    into("${layout.buildDirectory.get()}/classes/java/main/META-INF")
}

tasks.compileTestJava {
  dependsOn(tasks.jandex)
}

tasks.test {
  dependsOn(copyBeansXML)
}

tasks.classes {
  dependsOn(copyBeansXML)
}

tasks.processResources {
  dependsOn(copyBeansXML)
}

tasks.findByName("run")?.dependsOn(tasks.jandex, tasks.classes)
