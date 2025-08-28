package com.github.frederikpietzko.model

import java.time.OffsetDateTime

data class Visit(
  val id: Long?,
  val description: String,
  val pet: Pet,
  val dateTime: OffsetDateTime,
)
