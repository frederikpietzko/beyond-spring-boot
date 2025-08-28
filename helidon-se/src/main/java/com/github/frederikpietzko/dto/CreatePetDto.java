package com.github.frederikpietzko.dto;

import com.github.frederikpietzko.model.Pet;
import com.github.frederikpietzko.model.Type;

public record CreatePetDto(
  Long id,
  String name,
  int age,
  Type type) {

  public Pet toEntity() {
    return new Pet(id, name, age, type);
  }
}
