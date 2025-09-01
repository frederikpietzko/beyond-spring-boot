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
  initTables()
}
