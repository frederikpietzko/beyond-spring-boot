package com.github.frederikpietzko

import com.github.frederikpietzko.dto.CreateVisitDto
import com.github.frederikpietzko.dto.toDto
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.http.bodyAsClass

fun main() {
  setupDatabase()
  Javalin.create { config ->
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
  }.start(7070)
}
