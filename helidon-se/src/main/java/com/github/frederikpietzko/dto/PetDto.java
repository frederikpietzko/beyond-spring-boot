package com.github.frederikpietzko.dto;

import com.github.frederikpietzko.model.Pet;
import com.github.frederikpietzko.model.Type;


public record PetDto(
  Long id,
  String name,
  int age,
  Type type
) {
  public static PetDto fromEntity(Pet entity) {
    return new PetDto(entity.id(), entity.name(), entity.age(), entity.type());
  }
}
