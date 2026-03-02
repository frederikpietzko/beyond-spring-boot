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

fun main() {
  val yamlMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
  val configStream = Thread.currentThread().contextClassLoader.getResourceAsStream("application.yaml")
  val appConfig = yamlMapper.readTree(configStream)
  val dbConfig = appConfig.get("db")

  DbSettings.init(
    jdbcUrl = dbConfig.get("jdbcUrl").asText(),
    username = dbConfig.get("username").asText(),
    password = dbConfig.get("password").asText(),
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
    }
  }.start(8080)
}
