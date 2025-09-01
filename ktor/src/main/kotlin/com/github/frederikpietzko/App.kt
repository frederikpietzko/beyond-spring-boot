package com.github.frederikpietzko

import io.ktor.server.application.*

suspend fun Application.main() {
  setupDatabase()
  setupSerialization()
}
