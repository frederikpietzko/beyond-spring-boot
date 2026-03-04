package com.github.frederikpietzko

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.frederikpietzko.dto.CreateVisitDto
import com.github.frederikpietzko.dto.toDto
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.http.bodyAsClass
import io.javalin.json.JavalinJackson
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry

fun main() {
  val prometheusRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT).apply {
    config().commonTags("application", "javalin")
    ClassLoaderMetrics().bindTo(this)
    JvmMemoryMetrics().bindTo(this)
    JvmGcMetrics().bindTo(this)
    ProcessorMetrics().bindTo(this)
    JvmThreadMetrics().bindTo(this)
  }

  val yamlMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
  val configStream = Thread.currentThread().contextClassLoader.getResourceAsStream("application.yaml")
  val appConfig = yamlMapper.readTree(configStream)
  val dbConfig = appConfig.get("db")

  DbSettings.init(
    jdbcUrl = dbConfig.get("jdbcUrl").asText(),
    username = dbConfig.get("username").asText(),
    password = dbConfig.get("password").asText(),
    prometheusRegistry = prometheusRegistry,
  )

  val objectMapper = ObjectMapper().registerKotlinModule()
    .registerModule(JavaTimeModule())
  Javalin.create { config ->
    config.jsonMapper(JavalinJackson(objectMapper))
    config.router.apiBuilder {
      get("/visits") { ctx ->
        ctx.json(
          VisitRepository
            .getVisits()
            .map { it.toDto() },
        )
      }

      get("/visits/{id}") { ctx ->
        val id = ctx.pathParam("id").toLong()
        VisitRepository.findVisitById(id)
          ?.toDto()
          ?.let { ctx.json(it) }
          ?: ctx.status(404)
      }

      post("/visits") { ctx ->
        val req = ctx.bodyAsClass<CreateVisitDto>()
        VisitRepository.save(req.toModel())
          .toDto()
          .let { ctx.json(it) }
      }

      get("/metrics") { ctx ->
        ctx.contentType("text/plain").result(prometheusRegistry.scrape())
      }
    }
  }.start(8080)
}
