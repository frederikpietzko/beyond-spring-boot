package com.github.frederikpietzko.dto

import java.time.OffsetDateTime

data class VisitDto(
  val id: Long,
  val description: String,
  val pet: PetDto,
  val dateTime: OffsetDateTime,
)

fun com.github.frederikpietzko.model.Visit.toDto() = VisitDto(
  id = id!!,
  description = description,
  pet = pet.toDto(),
  dateTime = dateTime,
)
