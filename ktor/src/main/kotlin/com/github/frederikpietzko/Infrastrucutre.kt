package com.github.frederikpietzko

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*


fun Application.setupSerialization() {
  install(ContentNegotiation) {
    json()
  }
}

suspend fun Application.setupDatabase() {
  val jdbcUrl = System.getenv("JDBC_URL") ?: environment.config.property("db.jdbcUrl").getString()
  val username = System.getenv("DB_USERNAME") ?: environment.config.property("db.username").getString()
  val password = System.getenv("DB_PASSWORD") ?: environment.config.property("db.password").getString()

  DbSettings.init(jdbcUrl, username, password, appMicrometerRegistry)
}
