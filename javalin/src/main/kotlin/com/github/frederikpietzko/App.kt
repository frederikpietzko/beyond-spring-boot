package com.github.frederikpietzko

import com.fasterxml.jackson.databind.ObjectMapper
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
  setupDatabase()
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
