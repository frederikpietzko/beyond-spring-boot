package com.github.frederikpietzko.dto

import java.time.OffsetDateTime

data class CreateVisitDto(
  val description: String,
  val pet: CreatePetDto,
  val dateTime: OffsetDateTime,
) {
  fun toModel() = com.github.frederikpietzko.model.Visit(
    id = null,
    description = description,
    pet = pet.toModel(),
    dateTime = dateTime,
  )
}
