package com.github.frederikpietzko.dto;

import com.github.frederikpietzko.model.PetEntity;
import com.github.frederikpietzko.model.Type;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Serdeable.Serializable
public record PetDto(
  @NotNull Long id,
  @NotEmpty String name,
  @Min(1) int age,
  @NotNull Type type
) {
  public static PetDto fromEntity(PetEntity entity) {
    return new PetDto(entity.id, entity.name, entity.age, entity.type);
  }
}
