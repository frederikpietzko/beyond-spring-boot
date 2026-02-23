package com.github.frederikpietzko.dto;

import com.github.frederikpietzko.model.PetEntity;
import com.github.frederikpietzko.model.Type;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreatePetDto(
  @Nullable Long id,
  @NotEmpty String name,
  @Min(1) int age,
  @NotNull Type type) {

  public PetEntity toEntity() {
    final var pet = new PetEntity(name, age, type);
    pet.id = id;
    return pet;
  }
}
