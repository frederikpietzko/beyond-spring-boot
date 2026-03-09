package com.github.frederikpietzko

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.frederikpietzko.dto.CreateVisitDto
import com.github.frederikpietzko.dto.VisitDto
import com.github.frederikpietzko.dto.toDto
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.server.Helidon
import org.http4k.server.asServer

fun main() {
  val yamlMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
  val configStream = Thread.currentThread().contextClassLoader.getResourceAsStream("application.yaml")
  val appConfig = yamlMapper.readTree(configStream)
  val dbConfig = appConfig.get("db")

  val prometheusRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT).apply {
    config().commonTags("application", "http4k")
    ClassLoaderMetrics().bindTo(this)
    JvmMemoryMetrics().bindTo(this)
    JvmGcMetrics().bindTo(this)
    ProcessorMetrics().bindTo(this)
    JvmThreadMetrics().bindTo(this)
  }

  DbSettings.init(
    jdbcUrl = System.getenv("JDBC_URL") ?: dbConfig.get("jdbcUrl").asText(),
    username = System.getenv("DB_USERNAME") ?: dbConfig.get("username").asText(),
    password = System.getenv("DB_PASSWORD") ?: dbConfig.get("password").asText(),
    prometheusRegistry = prometheusRegistry,
  )

  val visitDtoListLens = Body.auto<List<VisitDto>>().toLens()
  val visitDtoLens = Body.auto<VisitDto>().toLens()
  val requestLens = Body.auto<CreateVisitDto>().toLens()
  val app: HttpHandler = routes(
    "/visits" bind routes(
      GET to {
        val visits = VisitRepository
          .getVisits()
          .map { it.toDto() }
        Response(OK)
          .with(visitDtoListLens of visits)
      },
      POST to { req ->
        val createVisit = requestLens(req)
        val visitDto = VisitRepository
          .save(createVisit.toModel())
          .toDto()
        Response(OK)
          .with(visitDtoLens of visitDto)
      },
    ),
    "/visits/{id}" bind GET to { req ->
      val id = req.path("id")!!.toLong()
      val visit = VisitRepository
        .findVisitById(id)
        .let { it?.toDto() }
      if (visit == null) {
        Response(NOT_FOUND)
      } else {
        Response(OK).with(visitDtoLens of visit)
      }
    },
    "/metrics" bind GET to {
      Response(OK).body(prometheusRegistry.scrape())
    },
  )

  app.asServer(Helidon(8080)).start()
}
