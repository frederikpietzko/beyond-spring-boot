package com.github.frederikpietzko.dto;

import com.github.frederikpietzko.model.Pet;
import com.github.frederikpietzko.model.Type;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PetDto(
  @NotNull Long id,
  @NotEmpty String name,
  @Min(1) int age,
  @NotNull Type type
) {
  public static PetDto fromEntity(Pet entity) {
    return new PetDto(entity.getId(), entity.getName(), entity.getAge(), entity.getType());
  }
}
