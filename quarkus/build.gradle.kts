plugins {
  java
  id("io.quarkus")
}


val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
  implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
  implementation("io.quarkus:quarkus-container-image-buildpack")
  implementation("io.quarkus:quarkus-hibernate-orm-panache")
  implementation("io.quarkus:quarkus-resteasy")
  implementation("io.quarkus:quarkus-resteasy-jackson")
  implementation("io.quarkus:quarkus-jdbc-postgresql")
  implementation("io.quarkus:quarkus-arc")
  implementation("io.quarkus:quarkus-hibernate-orm")
  implementation("io.quarkus:quarkus-hibernate-validator")
  compileOnly("org.projectlombok:lombok:1.18.38")
  annotationProcessor("org.projectlombok:lombok:1.18.38")
  testImplementation("io.quarkus:quarkus-junit5")
  testImplementation("io.rest-assured:rest-assured")
}

group = "com.github.frederikpietzko"
version = "1.0-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
  systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
tasks.withType<JavaCompile> {
  options.encoding = "UTF-8"
  options.compilerArgs.add("-parameters")
}
