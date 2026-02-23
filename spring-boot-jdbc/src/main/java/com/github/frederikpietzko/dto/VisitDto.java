package com.github.frederikpietzko.dto;

import com.github.frederikpietzko.model.VisitEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record VisitDto(
  @NotNull Long id,
  @NotEmpty String description,
  @NotNull PetDto pet,
  @NotNull OffsetDateTime dateTime
) {

  public static VisitDto fromEntity(VisitEntity entity) {
    return new VisitDto(
      entity.id,
      entity.description,
      PetDto.fromEntity(entity.pet),
      entity.dateTime
    );
  }
}
