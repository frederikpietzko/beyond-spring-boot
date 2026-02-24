package com.github.frederikpietzko.dto;

import com.github.frederikpietzko.model.Visit;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record VisitDto(
  @NotNull Long id,
  @NotEmpty String description,
  @NotNull PetDto pet,
  @NotNull OffsetDateTime dateTime
) {

  public static VisitDto fromEntity(Visit entity) {
    return new VisitDto(
      entity.getId(),
      entity.getDescription(),
      PetDto.fromEntity(entity.getPet()),
      entity.getDateTime()
    );
  }
}
