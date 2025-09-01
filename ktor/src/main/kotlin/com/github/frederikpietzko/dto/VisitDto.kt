package com.github.frederikpietzko.dto

import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class VisitDto(
  val id: Long,
  val description: String,
  val pet: PetDto,
  @Serializable(with = OffsetDateTimeSerializer::class) val dateTime: OffsetDateTime,
)

fun com.github.frederikpietzko.model.Visit.toDto() = VisitDto(
  id = id!!,
  description = description,
  pet = pet.toDto(),
  dateTime = dateTime,
)
