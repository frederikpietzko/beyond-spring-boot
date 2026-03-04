package com.github.frederikpietzko

import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics

suspend fun Application.main() {
  install(MicrometerMetrics) {
    registry = appMicrometerRegistry
    meterBinders = listOf(
      JvmMemoryMetrics(),
      JvmGcMetrics(),
      ProcessorMetrics(),
      JvmThreadMetrics(),
      ClassLoaderMetrics(),
    )
  }
  setupDatabase()
  setupSerialization()
  setupRouting()
}
