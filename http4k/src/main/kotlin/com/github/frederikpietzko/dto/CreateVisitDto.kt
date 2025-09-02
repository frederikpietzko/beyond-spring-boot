package com.github.frederikpietzko.dto

import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class CreateVisitDto(
  val description: String,
  val pet: CreatePetDto,
  @Serializable(with = OffsetDateTimeSerializer::class) val dateTime: OffsetDateTime,
) {
  fun toModel() = com.github.frederikpietzko.model.Visit(
    id = null,
    description = description,
    pet = pet.toModel(),
    dateTime = dateTime,
  )
}

