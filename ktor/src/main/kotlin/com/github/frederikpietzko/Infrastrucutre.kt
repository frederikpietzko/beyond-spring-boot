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
  val jdbcUrl = environment.config.property("db.jdbcUrl").getString()
  val username = environment.config.property("db.username").getString()
  val password = environment.config.property("db.password").getString()

  DbSettings.init(jdbcUrl, username, password, appMicrometerRegistry)
}
