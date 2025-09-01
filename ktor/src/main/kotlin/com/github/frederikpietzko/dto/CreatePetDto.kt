package com.github.frederikpietzko.dto

import com.github.frederikpietzko.model.Type
import kotlinx.serialization.Serializable

@Serializable
data class CreatePetDto(
  val id: Long?,
  val name: String,
  val age: Int,
  val type: Type,
) {
  fun toModel() = com.github.frederikpietzko.model.Pet(
    id = id,
    name = name,
    age = age,
    type = type,
  )
}

