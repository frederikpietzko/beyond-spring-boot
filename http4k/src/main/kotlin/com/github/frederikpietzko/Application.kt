package com.github.frederikpietzko

import com.github.frederikpietzko.dto.CreateVisitDto
import com.github.frederikpietzko.dto.VisitDto
import com.github.frederikpietzko.dto.toDto
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
import org.http4k.server.Undertow
import org.http4k.server.asServer

fun main() {
  setupDatabase()
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
      if(visit == null) {
        Response(NOT_FOUND)
      } else {
        Response(OK).with(visitDtoLens of visit)
      }
    },
  )

  app.asServer(Undertow(8080)).start()
}
