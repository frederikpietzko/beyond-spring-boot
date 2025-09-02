package com.github.frederikpietzko

import com.github.frederikpietzko.dto.CreateVisitDto
import com.github.frederikpietzko.dto.toDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

fun Application.setupRouting() {
  routing {
    route("/visits") {
      get {
        val visits = VisitRepository
          .getVisits()
          .map { it.toDto() }
        call.respond(visits)
      }
      get("/{id}") {
        val visit = call
          .parameters["id"]
          ?.toLongOrNull()
          ?.let { VisitRepository.findVisitById(it)?.toDto() }

        if (visit == null) {
          call.respond(HttpStatusCode.NotFound)
          return@get
        }

        call.respond(visit)
      }
      post {
        val createVisitDto = call.receive<CreateVisitDto>()
        VisitRepository.save(createVisitDto.toModel())
          .toDto()
          .let { call.respond(it) }
      }
    }
  }
}
