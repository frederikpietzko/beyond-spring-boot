package com.github.frederikpietzko.dto

data class PetDto(
  val id: Long,
  val name: String,
  val age: Int,
  val type: String,
)

fun com.github.frederikpietzko.model.Pet.toDto() = PetDto(
  id = id!!,
  name = name,
  age = age,
  type = type.name,
)
